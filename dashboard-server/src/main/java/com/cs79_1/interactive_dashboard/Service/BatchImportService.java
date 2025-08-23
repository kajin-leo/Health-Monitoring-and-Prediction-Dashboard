package com.cs79_1.interactive_dashboard.Service;

import com.cs79_1.interactive_dashboard.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


@Service
public class BatchImportService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void batchImport(MultipartFile file) throws Exception {
        List<String[]> csvData = parseCSV(file);
         try {
             // TODO: Create and save Users with metrics
         } catch (Exception e){

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

        return user;
    }

    // TODO: Methods of creating instances of metrics and save

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
}
