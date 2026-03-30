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

    /**
     * [1단계] 인가 코드로 액세스 토큰 받기
     */
    public String getAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        // HTTP Header 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        // ✅ 확인: x-www-form-urlencoded (대시 확인!)
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        // ✅ 확인: grant_type (언더바 확인!)
        params.add("grant_type", "authorization_code");
        params.add("client_id", REST_API_KEY);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        // Header와 Body를 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, httpHeaders);

        try {
            // ✅ 확인: kauth.kakao.com (.com 오타 수정 완료)
            ResponseEntity<String> response = rt.exchange(
                    "https://kauth.kakao.com/oauth/token",
                    HttpMethod.POST,
                    kakaoTokenRequest,
                    String.class
            );
            return response.getBody();
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            // 상세 에러 로그 출력 (401 에러 원인 파악용)
            System.out.println("에러 코드: " + e.getStatusCode());
            System.out.println("진짜 에러 이유: " + e.getResponseBodyAsString());
            throw e;
        }
    }

    /**
     * [2단계] 액세스 토큰으로 유저 정보 가져오기
     */
    public String getKaKaoUserInfo(String accessToken) {
        RestTemplate rt = new RestTemplate();

        // HTTP Header 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        // ✅ 확인: "Bearer " 뒤에 반드시 한 칸 띄우기!
        httpHeaders.add("Authorization", "Bearer " + accessToken);
        // ✅ 확인: application/x-www-form-urlencoded (오타 수정 완료)
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // 유저 정보 요청은 Body가 필요 없으므로 Header만 담음
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(httpHeaders);

        try {
            ResponseEntity<String> response = rt.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.POST,
                    kakaoProfileRequest,
                    String.class
            );

            System.out.println("카카오 유저 프로필 응답 성공!");
            return response.getBody();
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            System.out.println("유저 정보 가져오기 실패: " + e.getResponseBodyAsString());
            throw e;
        }
    }
}