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

    @Value("${gemini.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent}")
    private String geminiUrl;

    // 스프링 부트 내장 RestClient
    private final RestClient restClient = RestClient.create();

    // 1. Controller에서 호출할 수 있도록 public으로 변경
    public String generateResponse(String userPrompt) {
        // Gemini API 스펙에 맞는 요청 바디 구조 생성
        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(
                                Map.of("text", "사용자 요청: " + userPrompt + "\n\n위 요청에 대해 친근하고 자세한 국내 여행지 및 맞춤 일정으로 답변해주세요.")
                        ))
                )
        );

        try{
            // 2. URI에 ?key= 가 들어가도록 등호(=) 추가
            Map<String, Object> response = restClient.post()
                    .uri(geminiUrl + "?key=" + apikey)
                    .header("Content-Type", "application/json")
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
            // 3. 문장 끝에 세미콜론(;) 추가
            throw new RuntimeException("Gemini API 통신 오류 발생 : " + e.getMessage());
        }
    }
}