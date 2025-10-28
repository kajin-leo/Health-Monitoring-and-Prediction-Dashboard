package com.cs79_1.interactive_dashboard.Service;

import com.cs79_1.interactive_dashboard.DTO.Simulation.AlteredActivityPredictionRequest;
import com.cs79_1.interactive_dashboard.DTO.Simulation.PredictionResultDTO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class FlaskAPIService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${ML_PORT:5000}")
    private String mlPort;

    @Value("${ML_HOST:localhost}")
    private String mlHost;

    private String API_URL;
    
    @PostConstruct
    public void init() {
        this.API_URL = String.format("http://%s:%s/api/predict", mlHost, mlPort);
        log.info("API_URL initialized to: {}", API_URL);
    }

    public Object getHeatmap(long userId) {
        log.info("URL:{}", API_URL);

        String url = API_URL + "?sid=" + userId;
        return restTemplate.getForObject(url, Object.class);
    }

    public ResponseEntity<PredictionResultDTO> sendPredictionRequest(AlteredActivityPredictionRequest request) {
        String url = API_URL;
        return restTemplate.postForEntity(url, request, PredictionResultDTO.class);
    }
}
