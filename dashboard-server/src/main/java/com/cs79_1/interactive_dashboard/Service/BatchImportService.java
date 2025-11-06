package com.cs79_1.interactive_dashboard.Service;

import com.cs79_1.interactive_dashboard.DTO.BatchImport.FileInfo;
import com.cs79_1.interactive_dashboard.DTO.BatchImport.ImportProgress;
import com.cs79_1.interactive_dashboard.Entity.*;
import com.cs79_1.interactive_dashboard.Enum.HFZClassification;
import com.cs79_1.interactive_dashboard.Enum.MentalStrength;
import com.cs79_1.interactive_dashboard.Enum.Role;
import com.cs79_1.interactive_dashboard.Enum.WeightClassification;
import com.cs79_1.interactive_dashboard.Repository.*;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class BatchImportService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BodyMetricsRepository bodyMetricsRepository;

    @Autowired
    private BodyCompositionRepository bodyCompositionRepository;

    @Autowired
    private MentalHealthAndDailyRoutineRepository mentalHealthAndDailyRoutineRepository;

    @Autowired
    private WeeklyIntakeRepository weeklyIntakeRepository;

    @Autowired
    private WeightMetricsRepository weightMetricsRepository;

    @Autowired
    private WorkoutAmountRepository workoutAmountRepository;

    @Autowired
    private EntityManager entityManager;

    //register
    public record RegisterResult(Long userId, String username) {}
    private final ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    
    private final Map<String, ImportProgress> progressMap = new ConcurrentHashMap<>();

    public ImportProgress getProgress(String jobId){
        return progressMap.get(jobId);
    }

    public boolean isCompleted(String jobId){
        return progressMap.get(jobId).isCompleted();
    }

    public void initProgress(String jobId, ImportProgress importProgress){
        progressMap.put(jobId, importProgress);
    }

    @Transactional
    public void importIndividualAttributes(MultipartFile file) throws Exception {
        List<String[]> csvData = parseCSV(file);
        for(int i = 1; i < csvData.size(); i++){
            String[] row = csvData.get(i);
            try {
                if(userRepository.existsByUsername(row[0])) {
                    userRepository.deleteByUsername(row[0]);
                }

                User user = createUser(row);
                userRepository.save(user);

                saveBodyMetrics(user, row);
                saveBodyComposition(user, row);
                saveWeightMetrics(user, row);
                saveWeeklyIntake(user, row);
                saveMentalHealthAndDailyRoutine(user, row);
            } catch (Exception e){
                entityManager.clear();
                log.error("Error occurs when processing row " + (i+1) + ": " + e);
            }
        }
    }

    @Transactional
    public void importIndividualAttributesWithProgress(Path filePath, String jobId) {
        try {
            List<String[]> csvData = parseCSV(filePath);
            ImportProgress progress = getProgress(jobId);
            progress.setTotalNum(csvData.size() - 1);

            for(int i = 1; i < csvData.size(); i++){
                String[] row = csvData.get(i);
                try {
                    if(userRepository.existsByUsername(row[0])) {
                        userRepository.deleteByUsername(row[0]);
                    }

                    User user = createUser(row);
                    userRepository.save(user);

                    saveBodyMetrics(user, row);
                    saveBodyComposition(user, row);
                    saveWeightMetrics(user, row);
                    saveWeeklyIntake(user, row);
                    saveMentalHealthAndDailyRoutine(user, row);

                    progress.incrementProgress();
                    progress.setCurrent(i);
                    progress.setStatus("Processing " + i);
                } catch (Exception e){
                    entityManager.clear();
                    progress.incrementFailed();
                    progress.addError("Row " + i + ": " + e.getMessage());
                    log.error("Error occurs when processing row " + (i+1) + ": " + e);
                }
            }

            progress.setCompleted(true);
            progress.setSuccess(true);
        } catch (Exception e){
            ImportProgress progress = getProgress(jobId);
            if(progress != null){
                progress.setStatus("Failed " + e.getMessage());
                progress.setCompleted(true);
            }
            log.error("Import failed: ", e);
        }
    }

    @Transactional
    public void importMultipleWorkoutAmountDataWithProgress(List<FileInfo> fileInfos, String jobId) {
        try {
            ImportProgress progress = getProgress(jobId);
            progress.setTotalNum(fileInfos.size());

            for (int i = 0; i < fileInfos.size(); i++) {
                FileInfo fileInfo = fileInfos.get(i);
                Path filePath = fileInfo.getTempPath();
                try {
                    String fileName = fileInfo.getOriginalName();
                    if (fileName == null || !fileName.endsWith(".csv")) {
                        progress.incrementFailed();
                        progress.addError(fileName + " is not a csv file");
                        log.error("Skipping non-CSV file: " + fileName);
                        continue;
                    }

                    String participantId = fileName.replace(".csv", "");

                    importWorkoutAmountData(filePath, participantId, fileName);
                    progress.incrementProgress();
                    progress.setCurrent(i);
                    progress.setStatus("Processing " + i);
                    log.info("Successfully imported " + fileName);
                } catch (Exception e) {
                    progress.incrementFailed();
                    progress.addError("File " + i + "failed: " + e.getMessage());
                    log.error("Error importing " + fileInfo.getOriginalName() + ": " + e.getMessage());
                }
            }
            progress.setCompleted(true);
            progress.setSuccess(true);
        } catch (Exception e) {
            ImportProgress progress = getProgress(jobId);
            if(progress != null){
                progress.setStatus("Failed " + e.getMessage());
                progress.setCompleted(true);
            }
            log.error("Import failed: ", e);
        }
    }

    @Transactional
    public void importWorkoutAmountData(Path filePath, String participantId, String originalFileName) throws Exception {
        Optional<User> userOptional = userRepository.findByUsername(participantId);
        if (userOptional.isEmpty()){
            log.error(String.format("User with id %s not found", participantId));
            throw new Exception("User not found");
        }

        User user = userOptional.get();
        // Replace by deleting the existing data
        workoutAmountRepository.deleteByUserId(user.getId());

        List<String[]> csvData = parseCSV(filePath);
        for(int i = 1; i < csvData.size(); i++){
            String[] row = csvData.get(i);

            try {
                saveWorkoutAmount(user, row);
            } catch (Exception e){
                log.error("Error occurs when processing row " + (i+1) + "in file " + originalFileName + ": " + e.getMessage());
            }
        }
    }

    @Transactional
    public void importWorkoutAmountData(MultipartFile file, String participantId) throws Exception {
        Optional<User> userOptional = userRepository.findByUsername(participantId);
        if (userOptional.isEmpty()){
            log.error(String.format("User with id %s not found", participantId));
            throw new Exception("User not found");
        }

        User user = userOptional.get();
        // Replace by deleting the existing data
        workoutAmountRepository.deleteByUserId(user.getId());

        List<String[]> csvData = parseCSV(file);
        for(int i = 1; i < csvData.size(); i++){
            String[] row = csvData.get(i);

            try {
                saveWorkoutAmount(user, row);
            } catch (Exception e){
                log.error("Error occurs when processing row " + (i+1) + ": " + e.getMessage());
            }
        }
    }

    private User createUser(String[] row){
        User user = new User();
        user.setUsername(getStringValue(row, 0));

        String defaultPassword = "pwd@" + getStringValue(row, 0).substring(6);
        user.setPassword(passwordEncoder.encode(defaultPassword));
        user.setAgeMonth(getDoubleValue(row, 1));
        user.setAgeYear(getIntValue(row, 10));
        user.setSex(getIntValue(row, 2));
        user.setRole(Role.USER);

        return user;
    }

    private void saveBodyMetrics(User user, String[] row){
        BodyMetrics bodyMetrics = new BodyMetrics(user);

        bodyMetrics.setWaistSize(getDoubleValue(row, 3));
        bodyMetrics.setBicipital(getDoubleValue(row, 4));
        bodyMetrics.setTricipital(getDoubleValue(row, 5));
        bodyMetrics.setSupraIliac(getDoubleValue(row, 6));
        bodyMetrics.setSubscapularis(getDoubleValue(row, 7));
        bodyMetrics.setTannerStage(getDoubleValue(row, 8));
        bodyMetrics.setSkinFoldsSum(getDoubleValue(row, 9));
        bodyMetrics.setHeight(getDoubleValue(row, 17));
        bodyMetrics.setWeight(getDoubleValue(row, 18));

        bodyMetricsRepository.save(bodyMetrics);
    }

    private void saveBodyComposition(User user, String[] row){
        BodyComposition bodyComposition = new BodyComposition(user);

        bodyComposition.setFatPercentage(getDoubleValue(row, 11));
        bodyComposition.setHfzFatPercentage(mapToHfzClassification(getStringValue(row, 12)));
        bodyComposition.setFatMass(getDoubleValue(row, 13));
        bodyComposition.setHfzFatMass(mapToHfzClassification(getStringValue(row, 14)));
        bodyComposition.setBMI(getDoubleValue(row, 15));
        bodyComposition.setHfzBMI(mapToHfzClassification(getStringValue(row, 16)));
        bodyComposition.setFatAmount(getDoubleValue(row, 19));
        bodyComposition.setFfmAmount(getDoubleValue(row, 20));
        bodyComposition.setMuscleAmount(getDoubleValue(row, 21));
        bodyComposition.setWaterAmount(getDoubleValue(row, 22));
        bodyComposition.setWaterPercentage(getDoubleValue(row, 23));
        bodyComposition.setWlgr625(getDoubleValue(row, 24));
        bodyComposition.setWlgx625(getDoubleValue(row, 25));
        bodyComposition.setWlgr50(getDoubleValue(row, 26));
        bodyComposition.setWlgx50(getDoubleValue(row, 27));

        bodyCompositionRepository.save(bodyComposition);
    }

    private void saveWeightMetrics(User user, String[] row){
        WeightMetrics weightMetrics = new WeightMetrics(user);

        weightMetrics.setIotfL(getDoubleValue(row, 29));
        weightMetrics.setIotfM(getDoubleValue(row, 30));
        weightMetrics.setIotfS(getDoubleValue(row, 31));
        weightMetrics.setIotfZ(getDoubleValue(row, 32));
        weightMetrics.setIotfPercentile(getDoubleValue(row, 33));
        weightMetrics.setIotfClassification(mapToWeightClassification(getStringValue(row, 34)));

        weightMetrics.setCacheraL(getDoubleValue(row, 35));
        weightMetrics.setCacheraM(getDoubleValue(row, 36));
        weightMetrics.setCacheraS(getDoubleValue(row, 37));
        weightMetrics.setCacheraZ(getDoubleValue(row, 38));
        weightMetrics.setCacheraPercentile(getDoubleValue(row, 39));
        weightMetrics.setCacheraClassification(mapToWeightClassification(getStringValue(row, 40)));

        weightMetrics.setOmsL(getDoubleValue(row, 41));
        weightMetrics.setOmsM(getDoubleValue(row, 42));
        weightMetrics.setOmsS(getDoubleValue(row, 43));
        weightMetrics.setOmsZ(getDoubleValue(row, 44));
        weightMetrics.setOmsPercentile(getDoubleValue(row, 45));
        weightMetrics.setOmsClassification(mapToWeightClassification(getStringValue(row, 46)));

        weightMetrics.setCdcL(getDoubleValue(row, 47));
        weightMetrics.setCdcM(getDoubleValue(row, 48));
        weightMetrics.setCdcS(getDoubleValue(row, 49));
        weightMetrics.setCdcZ(getDoubleValue(row, 50));
        weightMetrics.setCdcPercentile(getDoubleValue(row, 51));
        weightMetrics.setCdcClassification(mapToWeightClassification(getStringValue(row, 52)));

        weightMetricsRepository.save(weightMetrics);
    }

    private void saveWeeklyIntake(User user, String[] row){
        WeeklyIntake weeklyIntake = new WeeklyIntake(user);

        weeklyIntake.setCereals(getDoubleValue(row, 62));
        weeklyIntake.setVegetablesAndLegumes(getDoubleValue(row, 63));
        weeklyIntake.setFruit(getDoubleValue(row, 64));
        weeklyIntake.setDairy(getDoubleValue(row, 65));
        weeklyIntake.setFatsOils(getDoubleValue(row, 66));
        weeklyIntake.setMeatFishPoultryEggs(getDoubleValue(row, 67));
        weeklyIntake.setDrinks(getDoubleValue(row, 68));
        weeklyIntake.setExtras(getDoubleValue(row, 69));
        weeklyIntake.setOther(getDoubleValue(row, 70));
        weeklyIntake.setWater(getDoubleValue(row, 71));
        weeklyIntake.setSugarSweetenedBeverages(getDoubleValue(row, 72));
        weeklyIntake.setEnergyGroupAvgDaily(getDoubleValue(row, 73));
        weeklyIntake.setProtectiveGroupAvgDaily(getDoubleValue(row, 74));
        weeklyIntake.setBodybuildingGroupAvgDaily(getDoubleValue(row, 75));
        weeklyIntake.setLimitedFoodAvgDaily(getDoubleValue(row, 76));
        weeklyIntake.setLimitedBeveragesAvgDaily(getDoubleValue(row, 77));
        weeklyIntake.setWaterAvgDaily(getDoubleValue(row, 78));

        weeklyIntakeRepository.save(weeklyIntake);
    }

    private void saveMentalHealthAndDailyRoutine(User user, String[] row){
        MentalHealthAndDailyRoutine mentalHealthAndDailyRoutine = new MentalHealthAndDailyRoutine(user);

        mentalHealthAndDailyRoutine.setSelfesteemScore(getDoubleValue(row, 79));
        mentalHealthAndDailyRoutine.setSelfesteemStrength(mapToMentalStrength(getStringValue(row, 80)));
        mentalHealthAndDailyRoutine.setProcrastinationScore(getDoubleValue(row, 81));
        mentalHealthAndDailyRoutine.setProcrastinationStrength(mapToMentalStrength(getStringValue(row, 82)));
        mentalHealthAndDailyRoutine.setWeekdaySleepingAvgDuration(getDoubleValue(row, 83));
        mentalHealthAndDailyRoutine.setWeekendSleepingAvgDuration(getDoubleValue(row, 84));
        mentalHealthAndDailyRoutine.setTotalSleepingDuration(getDoubleValue(row, 85));

        mentalHealthAndDailyRoutineRepository.save(mentalHealthAndDailyRoutine);
    }

    public void saveWorkoutAmount(User user, String[] row){
        WorkoutAmount workoutAmount = new WorkoutAmount(user);

        workoutAmount.setDay(getIntValue(row, 0));
        workoutAmount.setHour(getIntValue(row, 1));
        workoutAmount.setSumSecondsMVPA3(getIntValue(row, 2));
        workoutAmount.setTimesMVPA3(getIntValue(row, 3));
        workoutAmount.setSumSecondsSED60(getIntValue(row, 4));
        workoutAmount.setTimesSED60(getIntValue(row, 5));
        workoutAmount.setSumSecondsLight3(getIntValue(row, 6));
        workoutAmount.setTimesLight3(getIntValue(row, 7));
        workoutAmount.setDateTime(parseDateTime(getStringValue(row, 8)));

        workoutAmountRepository.save(workoutAmount);
    }

    private String getStringValue(String[] row, int index){
        if (index >= row.length || row[index] == null) {
            return null;
        }
        return row[index].trim();
    }

    private double getDoubleValue(String[] row, int index){
        if (index >= row.length || row[index] == null || row[index].trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(row[index].trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private int getIntValue(String[] row, int index){
        if (index >= row.length || row[index] == null || row[index].trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(row[index].trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private LocalDateTime parseDateTime(String dateTime){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return LocalDateTime.parse(dateTime, formatter);
        } catch (Exception e) {
            log.error("Error parsing dateTime: " + dateTime);
            return LocalDateTime.now();
        }
    }

    private List<String[]> parseCSV(Path filePath) throws Exception {
        List<String[]> csvData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(filePath), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                for(int i = 0; i < values.length; i++) {
                    values[i] = values[i].replaceAll("^\"|\"$", "").trim();
                }

                csvData.add(values);
            }
        }

        return csvData;
    }

    private List<String[]> parseCSV(MultipartFile file) throws Exception {
        List<String[]> csvData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                for(int i = 0; i < values.length; i++) {
                    values[i] = values[i].replaceAll("^\"|\"$", "").trim();
                }

                csvData.add(values);
            }
        }

        return csvData;
    }

    private List<String[]> parseCSV(byte[] fileContent) throws Exception {
        List<String[]> csvData = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileContent), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                for(int i = 0; i < values.length; i++) {
                    values[i] = values[i].replaceAll("^\"|\"$", "").trim();
                }

                csvData.add(values);
            }
        }

        return csvData;
    }

    private HFZClassification mapToHfzClassification(String value){
        if (value == null || value.trim().isEmpty()){
            return HFZClassification.HFZ; // Return default value
        }

        switch (value.toUpperCase().trim()) {
            case "HFZ":
                return HFZClassification.HFZ;
            case "NI":
                return HFZClassification.NI;
            case "NIHR":
                return HFZClassification.NIHR;
            case "VL":
                return HFZClassification.VL;
            default:
                return HFZClassification.HFZ;
        }
    }

    private WeightClassification mapToWeightClassification(String value){
        if (value == null || value.trim().isEmpty()){
            return WeightClassification.Normal;
        }

        switch (value.toUpperCase().trim()) {
            case "NORMAL":
                return WeightClassification.Normal;
            case "SURPOIDS":
                return WeightClassification.Surpoids;
            case "OBESE":
                return WeightClassification.Obese;
            case "MAIGRE":
                return WeightClassification.Maigre;
            default:
                return WeightClassification.Normal;
        }
    }

    private MentalStrength mapToMentalStrength(String value){
        if (value == null || value.trim().isEmpty()){
            return MentalStrength.NaN;
        }

        switch (value.toUpperCase().trim()) {
            case "HIGH":
                return MentalStrength.High;
            case "MEDIUM":
                return MentalStrength.Medium;
            case "LOW":
                return MentalStrength.Low;
            case "NAN":
                return MentalStrength.NaN;
            default:
                return MentalStrength.NaN;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public RegisterResult registerWithExerciseData(String username, String rawPassword, MultipartFile jsonFile) {
        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }

        // parse JSON（Just read the memory stream; if the file is large, switch to streaming/temporary file）
        JsonNode root;
        try (InputStream in = jsonFile.getInputStream()) {
            root = mapper.readTree(in);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid JSON file");
        }

        User u = new User();
        u.setUsername(username);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRole(Role.USER);
        User saved = userRepository.save(u);
        Long userId = saved.getId();

        // BodyComposition
        double bmi = root.path("basicMetrics").path("bmi").asDouble(Double.NaN);
        System.out.println("bmi:" + bmi);
        if (!Double.isNaN(bmi)) {
            BodyComposition bc = new BodyComposition(u);
            bc.setBMI(bmi);
            bodyCompositionRepository.save(bc);
        } else {
            // throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "basicMetrics.bmi missing");
        }

        // BodyMetrics
        JsonNode bmNode = root.path("basicMetrics");
        Double height = bmNode.has("height") ? bmNode.get("height").asDouble() : null;
        Double weight = bmNode.has("weight") ? bmNode.get("weight").asDouble() : null;
        if (height != null || weight != null) {
            BodyMetrics bm = new BodyMetrics(u);
            if (height != null) bm.setHeight(height);
            if (weight != null) bm.setWeight(weight);
            bodyMetricsRepository.save(bm);
        }

        // Mental
        JsonNode sleep = root.path("sleepStatistics");
        if (!sleep.isMissingNode() && !sleep.isNull()) {
            MentalHealthAndDailyRoutine m = new MentalHealthAndDailyRoutine(u);
            if (sleep.has("weekdaysAvg")) m.setWeekdaySleepingAvgDuration(sleep.get("weekdaysAvg").asDouble());
            if (sleep.has("weekendsAvg")) m.setWeekendSleepingAvgDuration(sleep.get("weekendsAvg").asDouble());
            if (sleep.has("totalAvg"))    m.setTotalSleepingDuration(sleep.get("totalAvg").asDouble());
            mentalHealthAndDailyRoutineRepository.save(m);
        }

        // WorkoutAmount
        JsonNode actArr = root.path("activityData");
        if (actArr.isArray()) {
            DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            List<WorkoutAmount> batch = new ArrayList<>(actArr.size());
            for (JsonNode n : actArr) {
                WorkoutAmount wa = new WorkoutAmount(u);
                if (n.hasNonNull("date")) {
                    String s = n.get("date").asText();
                    try { wa.setDateTime(LocalDateTime.parse(s, F)); }
                    catch (Exception ignored) {}
                }
                if (n.hasNonNull("day"))  wa.setDay(n.get("day").asInt() + 1);    // 1..7
                if (n.hasNonNull("hour")) wa.setHour(n.get("hour").asInt());  // 0..23

                if (n.has("mvpaSeconds"))  wa.setSumSecondsMVPA3(n.get("mvpaSeconds").asInt());
                if (n.has("lightSeconds")) wa.setSumSecondsLight3(n.get("lightSeconds").asInt());
                if (n.has("mvpaTimes"))    wa.setTimesMVPA3(n.get("mvpaTimes").asInt());
                if (n.has("lightTimes"))   wa.setTimesLight3(n.get("lightTimes").asInt());

                batch.add(wa);
            }
            workoutAmountRepository.saveAll(batch);
        }

        return new RegisterResult(userId, saved.getUsername());
    }
}

