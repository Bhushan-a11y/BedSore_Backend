package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Service
public class AIService {

    @Value("${gemini.api.url}")
    private String geminiUrl;

    public String generateRecommendation(String prompt) {
        RestTemplate restTemplate = new RestTemplate();
        
        // This is the specific JSON structure Gemini expects
        String jsonBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}]}]}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

        try {
            // Firing the request
            String response = restTemplate.postForObject(geminiUrl, entity, String.class);
            return response; // You will need to parse this JSON to get just the text!
        } catch (Exception e) {
            return "AI Service is currently offline: " + e.getMessage();
        }
    }
}
