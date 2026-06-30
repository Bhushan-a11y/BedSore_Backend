package com.example.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import org.springframework.http.*;

@Service
public class AIService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${gemini.api.url}")
    private String geminiUrl;
public String generateRecommendation(String prompt) {

  
    String jsonBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}]}]}";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

    try {

        String response = restTemplate.postForObject(
                geminiUrl,
                entity,
                String.class
        );

       
        JsonNode root = objectMapper.readTree(response);

        JsonNode candidates = root.path("candidates");

        if (candidates.isArray() && candidates.size() > 0) {

            return candidates.get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asString();

        }

        return "No recommendation generated.";

    } catch (Exception e) {
        e.printStackTrace();
        return "AI Service is currently offline.";
    }
}
}
