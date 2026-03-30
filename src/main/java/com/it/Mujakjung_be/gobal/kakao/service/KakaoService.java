package com.it.Mujakjung_be.gobal.kakao.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Slf4j
@Service
public class KakaoService {

    private final String REST_API_KEY = "c20fa1e751278dc7d481f42f175401b2";
    private final String REDIRECT_URI = "http://localhost:8080/auth/kakao/callback";

    // [1단계] 인가 코드로 액세스 토큰 받기
    public String getAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        // HTTP Header 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        // ⚠️ 수정: x-www.form (X) -> x-www-form (O) 점(.)이 아니라 대시(-)여야 해!
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        // ⚠️ 수정: grant-type (X) -> grant_type (O) 대시(-)가 아니라 언더바(_)야!
        params.add("grant_type", "authorization_code");
        params.add("client_id", REST_API_KEY);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        // Header와 Body를 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, httpHeaders);

        // ⚠️ 수정: .comm (X) -> .com (O) 오타 수정
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        System.out.println("카카오 토큰 응답: " + response.getBody());
        return response.getBody();
    }

    // [2단계] 액세스 토큰으로 유저 정보 가져오기
    public String getKaKaoUserInfo(String accessToken) {
        RestTemplate rt = new RestTemplate();

        // HTTP Header 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        // ⚠️ 수정: "Bearer" + accessToken (X) -> "Bearer " + accessToken (O) 한 칸 띄워야 해!
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        // ⚠️ 수정: from- url (X) -> form-url (O) 오타와 공백 제거
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(httpHeaders);

        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        System.out.println("카카오 유저 프로필 응답: " + response.getBody());
        return response.getBody();
    }
}