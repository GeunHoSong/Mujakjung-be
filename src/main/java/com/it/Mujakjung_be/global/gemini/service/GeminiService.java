package com.it.Mujakjung_be.global.gemini.service;

import com.google.genai.Client;
import com.google.genai.models.GenerateContentResponse; // 최신 SDK용 import
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GeminiService {

    private final Client client;

    public GeminiService(@Value("${gemini.api-key}") String apiKey) {
        // 최신 SDK 방식: Client 객체 생성
        this.client = Client.builder().apiKey(apiKey).build();
    }

    public String generateTravelPlan(String message) {
        try {
            // gemini-2.0-flash 모델을 사용하여 호출
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.0-flash",
                    message
            );
            return response.text();
        } catch (Exception e) {
            return "AI 응답 오류: " + e.getMessage();
        }
    }
}