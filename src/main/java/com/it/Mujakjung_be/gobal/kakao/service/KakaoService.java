package com.it.Mujakjung_be.gobal.kakao.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.Mujakjung_be.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.it.Mujakjung_be.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final String REST_API_KEY = "c20fa1e751278dc7d481f42f175401b2";
    private final String REDIRECT_URI = "http://localhost:8080/auth/kakao/callback";
    private final UserRepository repository;
    /**
     * [1단계] 인가 코드로 액세스 토큰 받기
     */
    public String getAccessToken(String code) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", REST_API_KEY);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, httpHeaders);

        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.getBody());

            return jsonNode.get("access_token").asText();

        } catch (Exception e) {
            throw new RuntimeException("토큰 파싱 실패", e);
        }
    }


    /**
     * [2단계] 액세스 토큰으로 유저 정보 가져오기
     */
    public String getUserInfo
    (String accessToken) {
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

    public void saveUser(String userInfo) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(userInfo);
            Long kakaoid = jsonNode.get("id").asLong();

            // db 조회
            User user = repository.findByKakaoId(kakaoid);

            // 없으면 저장
            if (user == null) {
                User newUser = new User();
                newUser.setKakaoId(kakaoid);;

                repository.save(newUser);
            } else {
                System.out.println("이미 가입된 회원");
            }

        } catch (Exception e) {
            throw new RuntimeException("유저 저장 실패");
        }
    }


}