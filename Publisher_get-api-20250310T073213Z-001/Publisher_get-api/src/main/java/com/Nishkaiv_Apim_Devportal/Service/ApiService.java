package com.Nishkaiv_Apim_Devportal.Service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;

@Service
public class ApiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private static final String API_BASE_URL = "https://api.kriate.co.in:8344/api/am/publisher/v4/apis";

    @Autowired
    public ApiService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    /**
     * Fetch Bearer Token from the Token API
     */
    private String fetchNewToken() {
        String tokenUrl = "https://api.kriate.co.in:8344/oauth2/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic YVFDcjR4ajhnVU9WUXBBcTFra3ozbWR5WkZvYTpmbHRZaHFrcG90NEY3R2VXZmp1QVRXU1BjY1lh");

        String requestBody = "grant_type=password&username=admin&password=admin&scope=apim:api_create apim:api_manage";
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response = restTemplate.exchange(tokenUrl, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().get("access_token").toString();
        } else {
            throw new RuntimeException("Failed to fetch token");
        }
    }

    /**
     * Fetch API details using the Bearer Token
     */
    public ResponseEntity<Map<String, Object>> fetchApis(String apiId) {
        try {
            // Step 1: Fetch Bearer Token
            String bearerToken = fetchNewToken();

            // Step 2: Construct API URL by appending apiId directly
            String apiUrl = API_BASE_URL + "/" + apiId;

            // Step 3: Set headers with Bearer Token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + bearerToken);

            // Step 4: Make API request
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);

            // Step 5: Parse JSON response
            Map<String, Object> jsonResponse = objectMapper.readValue(response.getBody(), Map.class);
            return ResponseEntity.ok(jsonResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to fetch API details"));
        }
    }
}
