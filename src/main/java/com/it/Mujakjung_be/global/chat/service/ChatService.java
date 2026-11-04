
package com.it.Mujakjung_be.global.chat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.util.Map;

@Service
public class ChatService {

    private final RestClient restClient;

    public ChatService(
            @Value("${fastapi.url:http://127.0.0.1:8001}")
            String fastApiUrl
    ) {
        System.out.println("FastAPI 서버 주소: " + fastApiUrl);

        this.restClient = RestClient.builder()
                .baseUrl(fastApiUrl)
                .requestFactory(new SimpleClientHttpRequestFactory())
                .build();
    }

    public String generateResponse(String userPrompt) {

        Map<String, String> requestBody = Map.of(
                "content", userPrompt
        );

        System.out.println("FastAPI 요청 데이터: " + requestBody);

        Map response = restClient.post()
                .uri("/api/chat")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(Map.class);

        if (response == null ||
                !(response.get("analysisResult") instanceof String)) {
            throw new IllegalStateException(
                    "FastAPI 상담 응답 형식이 올바르지 않습니다."
            );
        }

        return (String) response.get("analysisResult");
    }
}
