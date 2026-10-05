package com.it.Mujakjung_be.global.member.util;

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

    private final String CLIENT_ID = "0mtzJI9Tavpqok3pG5Rw";
    private final String CLIENT_SECRET = "T5A3eve1sR";

    /**
     * 1. 네이버로부터 인가 코드를 받아 Access Token을 요청하는 메서드
     */
    public String getAccessToken(String code, String state) {
        String tokenUrl = "https://nid.naver.com/oauth2.0/token";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", CLIENT_ID);
        params.add("client_secret", CLIENT_SECRET);
        params.add("code", code);
        params.add("state", state);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(tokenUrl, request, String.class);
            System.out.println("[NaverUtil DEBUG] 네이버 토큰 원본 응답 -> " + response.getBody());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            // 💡 1. 네이버가 에러 응답을 주면 null을 주지 않고 즉시 예외를 발생시켜 흐름을 정지시킵니다.
            if (jsonNode.has("error")) {
                String error = jsonNode.get("error").asText();
                String errorDesc = jsonNode.has("error_description") ? jsonNode.get("error_description").asText() : "";
                throw new RuntimeException("네이버 인증 서버 에러: " + error + " (" + errorDesc + ")");
            }

            // 💡 2. 정상적으로 토큰 필드가 있을 때만 검증 후 리턴합니다.
            if (jsonNode.has("access_token")) {
                String token = jsonNode.get("access_token").asText();

                if (token == null || token.isEmpty() || token.equalsIgnoreCase("null")) {
                    throw new RuntimeException("네이버가 준 토큰 값이 비어있거나 올바르지 않습니다.");
                }

                System.out.println("⭕ 정상 발급된 네이버 Access Token: " + token);
                return token;
            }

            throw new RuntimeException("네이버 응답에 access_token 필드가 존재하지 않습니다.");

        } catch (Exception e) {
            throw new RuntimeException("네이버 엑세스 토큰 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }

    /**
     * 2. 발급받은 Access Token을 가지고 네이버 유저 프로필 정보를 가져오는 메서드
     */
    public NaverDto getNaverUserInfo(String accessToken) {
        // 💡 이제 이 안전장치는 앞 단계에서 100% 검증된 토큰만 넘어오므로 절대 터질 일이 없습니다!
        if (accessToken == null || accessToken.isEmpty() || accessToken.equalsIgnoreCase("null")) {
            throw new RuntimeException("유효한 Access Token이 없어서 네이버 프로필을 요청할 수 없습니다.");
        }

        String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, String.class);
            System.out.println("[NaverUtil DEBUG] 프로필 원본 응답 -> " + response.getBody());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            JsonNode naverResponse = jsonNode.get("response");
            if (naverResponse == null) {
                String errorMsg = jsonNode.has("message") ? jsonNode.get("message").asText() : "알 수 없는 에러";
                throw new RuntimeException("네이버 프로필 파싱 실패: " + errorMsg);
            }

            NaverDto naverDto = new NaverDto();
            naverDto.setResultcode(jsonNode.has("resultcode") ? jsonNode.get("resultcode").asText() : null);
            naverDto.setMessage(jsonNode.has("message") ? jsonNode.get("message").asText() : null);

            NaverDto.Response responseObj = new NaverDto.Response();
            if (naverResponse.has("email")) responseObj.setEmail(naverResponse.get("email").asText());
            if (naverResponse.has("name")) responseObj.setName(naverResponse.get("name").asText());
            if (naverResponse.has("id")) responseObj.setId(naverResponse.get("id").asText());

            naverDto.setResponse(responseObj);
            return naverDto;

        } catch (Exception e) {
            throw new RuntimeException("네이버 유저 정보를 가져오는데 실패했습니다.", e);
        }
    }
}