package com.it.Mujakjung_be.global.chat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    @Value("${gemini.api-key}")
    private String apikey;

    @Value("${gemini.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-3.8-flash:generateContent}")
    private String geminiUrl;

    // 스프링 부트 내장 RestClient
    private final RestClient restClient = RestClient.create();

    public String generateResponse(String userPrompt) {
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", "사용자 요청: " + userPrompt + "\n\n위 요청에 대해 친근하고 자세한 국내 여행지 및 맞춤 일정으로 답변해주세요.")
                        ))
                )
        );

        try {
            // 💡 URI에는 ?key=를 붙이지 않고, 헤더(x-goog-api-key)로 API Key를 전송
            Map<String, Object> response = restClient.post()
                    .uri(geminiUrl)
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", apikey) // 👈 헤더로 API Key 전달
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);

            if(response != null && response.containsKey("candidates")){
                List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                if(!candidates.isEmpty()){
                    Map<String, Object> content = (Map<String, Object>) candidates.get(0).get("content");
                    List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                    if(!parts.isEmpty()){
                        return (String) parts.get(0).get("text");
                    }
                }
            }
            return "AI 응답을 받아오지 못했습니다.";

        } catch (Exception e) {
            throw new RuntimeException("Gemini API 통신 오류 발생 : " + e.getMessage());
        }
    }
}