package com.haryana.roadways.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

@Service
public class EtaService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String FLASK_URL = "http://127.0.0.1:5000/predict-eta";

    public double getPredictedEta(double distance, double speed, int traffic, int hour, int day, int stops) {
        try {
            EtaRequest requestPayload = new EtaRequest(distance, speed, traffic, hour, day, stops);
            
            ResponseEntity<EtaResponse> response = restTemplate.postForEntity(
                FLASK_URL, 
                requestPayload, 
                EtaResponse.class
            );

            if (response.getBody() != null && response.getBody().isSuccess()) {
                return response.getBody().getEstimated_eta_minutes();
            }
        } catch (Exception e) {
            System.err.println("Error calling Flask ML service: " + e.getMessage());
        }
        return -1.0; // Fallback agar prediction fail ho jaye
    }
}