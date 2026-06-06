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

    private final String CLIENT_ID = "0mtzJI9Tavpqok3pG5Rw";

    // 🚨 [필독] 네이버 개발자 센터(내 애플리케이션 -> 개요)에서
    // "Client Secret" 보기 버튼을 눌러서 '20자리' 문자열을 정확하게 다시 복사해서 붙여넣어줘!
    private final String CLIENT_SECRET = "T5A3eve1sR";

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
            System.out.println("DEBUG: 네이버 토큰 응답 -> " + response.getBody());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            if (jsonNode.has("access_token")) {
                return jsonNode.get("access_token").asText();
            } else {
                String errorMsg = jsonNode.has("error_description") ?
                        jsonNode.get("error_description").asText() : "알 수 없는 에러";
                System.err.println("❌ 네이버 로그인 토큰 발급 실패! 원인: " + errorMsg);
                return null;
            }

        } catch (Exception e) {
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
        System.out.println("DEBUG: 사용 하려는 Access Token 값 -> [" + accessToken + "]");

        try {
            ResponseEntity<String> response = restTemplate.exchange(userInfoUrl, HttpMethod.GET, request, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            // 네이버 응답에서 "response" 노드를 가져옵니다.
            JsonNode naverResponse = jsonNode.get("response");

            if (naverResponse == null) {
                String errorMsg = jsonNode.has("message") ? jsonNode.get("message").asText() : "알 수 없는 에러";
                System.err.println("❌ 네이버 프로필 가져오기 실패: " + errorMsg);
                throw new RuntimeException("네이버 프로필 정보를 가져오는데 실패했습니다: " + errorMsg);
            }

            // 1. 최상위 NaverDto 객체 생성
            NaverDto naverDto = new NaverDto();
            naverDto.setResultcode(jsonNode.has("resultcode") ? jsonNode.get("resultcode").asText() : null);
            naverDto.setMessage(jsonNode.has("message") ? jsonNode.get("message").asText() : null);

            // 2. 계층형 이너 클래스인 Response 객체 생성 및 값 세팅 (★컴파일 에러 해결 지점!)
            NaverDto.Response responseObj = new NaverDto.Response();
            if (naverResponse.has("email")) responseObj.setEmail(naverResponse.get("email").asText());
            if (naverResponse.has("name")) responseObj.setName(naverResponse.get("name").asText());
            if (naverResponse.has("id")) responseObj.setId(naverResponse.get("id").asText());

            // 3. NaverDto에 완성된 responseObj를 쏙 집어넣기
            naverDto.setResponse(responseObj);

            return naverDto;

        } catch (Exception e) {
            throw new RuntimeException("네이버 유저 정보를 가져오는데 실패했습니다.", e);
        }
    }
}