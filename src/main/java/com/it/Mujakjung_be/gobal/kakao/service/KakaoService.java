package com.it.Mujakjung_be.gobal.kakao.service;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;

@Slf4j
@Service
public class KakaoService {

    private final String REST_API_KEY = "c20fa1e751278dc7d481f42f175401b2";
    private final String REDIRECT_URI = "http://localhost:8080/auth/kakao/callback";
    // 인가 코드 로 엑세스 토큰 받기
    public String getAccessToken(String code){
        RestTemplate rt = RestTemplate();

        // Http header 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www.form-urlencoded;charset=utf-8");
        // HTTP Body 생성 (카카오가 요구하는 필수 파라미터들)
        MultiValueMap<String , String> params = new LinkedMultiValueMap<>();
        params.add("grant-type", "authorization_code");
        params.add("client_id", REST_API_KEY);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);
        // Header와 Body를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String , String>> kakaoTokenRequest = new HttpEntity<>(params, httpHeaders);
        // 카카오 서버로 POST 요청 보내고 응답 받기
        ResponseEntity<String> response = rt.exchange("")




    }
}
