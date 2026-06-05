package com.it.Mujakjung_be.global.naver.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.Mujakjung_be.global.naver.dto.NaverDto;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class NaverUtil {

    private final String CLIENT_ID = "0mtzJI9Tavpqok3pG5Rw"; // 👈 컨트롤러와 동일한 ID
    private final String CLIENT_SECRET = "T5A3eve1sR"; // 👈 네이버 개발자 센터에서 발급받은 Secret 입력!

    /**
     * 1. 네이버로부터 인가 코드를 받아 Access Token을 요청하는 메서드
     */
    public String getAccessToken(String code, String state) {
        String tokenUrl = "https://nid.naver.com/oauth2.0/token";

        RestTemplate restTemplate = new RestTemplate();

        // HTTP Header 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // HTTP Body 설정
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("code", code);
        params.add("state", state);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);

            // 1. 네이버 응답 본문을 읽어옴
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());
            if (jsonNode.has("access_token")) {
                return jsonNode.get("access_token").asText();
            } else {
                // 멈추지 않고 로그만 띄움!
                String errorMsg = jsonNode.has("error_description") ?
                        jsonNode.get("error_description").asText() : "알 수 없는 에러";
                System.err.println("❌ 네이버 로그인 실패! 원인: " + errorMsg);

                // 에러를 던지지 않고, 일단 빈 값을 리턴하거나 로그인을 취소하도록 유도
                return null;
            }

        } catch (Exception e) {
            // 네트워크 자체가 끊겼거나, JSON 파싱 자체가 실패했을 때 여기로 옴
            throw new RuntimeException("네이버 엑세스 토큰 통신 중 문제 발생", e);
        }
    }

    /**
     * 2. 발급받은 Access Token을 가지고 네이버 유저 프로필 정보를 가져오는 메서드
     */
    public NaverDto getNaverUserInfo(String accessToken) {
        String userInfoUrl = "https://openapi.naver.com/v1/nid/me";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            // 네이버 응답에서 "response" 노드를 가져옵니다.
            JsonNode naverResponse = jsonNode.get("response");

            // [핵심] 만약 네이버가 에러를 줘서 "response"가 없다면?
            if (naverResponse == null) {
                String errorMsg = jsonNode.has("message") ? jsonNode.get("message").asText() : "알 수 없는 에러";
                System.err.println("❌ 네이버 프로필 가져오기 실패: " + errorMsg);
                throw new RuntimeException("네이버 프로필 정보를 가져오는데 실패했습니다: " + errorMsg);
            }

            NaverDto naverDto = new NaverDto();
            // 각 필드가 null인지 확인하며 안전하게 가져오기
            if (naverResponse.has("email")) naverDto.setEmail(naverResponse.get("email").asText());
            if (naverResponse.has("name")) naverDto.setName(naverResponse.get("name").asText());
            if (naverResponse.has("id")) naverDto.setId(naverResponse.get("id").asText());

            return naverDto;

        } catch (Exception e) {
            throw new RuntimeException("네이버 유저 정보를 가져오는데 실패했습니다.", e);
        }
    }
}