package com.it.Mujakjung_be.global.kakao.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.Mujakjung_be.global.member.repository.MemberRepository;
import com.it.Mujakjung_be.global.member.util.JwtProvider;
import com.it.Mujakjung_be.global.user.entity.User;
import com.it.Mujakjung_be.global.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional // 클래스 전체에 트랜잭션 적용
public class KakaoService {

    private final String REST_API_KEY = "c20fa1e751278dc7d481f42f175401b2";
    private final String REDIRECT_URI = "http://localhost:8080/auth/kakao/callback";
    private final UserRepository userRepository; // User 관련 저장소
    private final JwtProvider jwtProvider;

    // [1단계] 인가 코드로 액세스 토큰 받기
    public String getAccessToken(String code) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", REST_API_KEY);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = rt.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, request, String.class);

        try {
            return new ObjectMapper().readTree(response.getBody()).get("access_token").asText();
        } catch (Exception e) {
            throw new RuntimeException("카카오 토큰 파싱 실패");
        }
    }

    // [2단계] 액세스 토큰으로 유저 정보 가져오기
    public String getUserInfo(String accessToken) {
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        return rt.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, request, String.class).getBody();
    }

    // [3단계] 유저 DB 저장 (이미 있으면 패스)
    public void saveUser(String userInfo) {
        try {
            Long kakaoid = new ObjectMapper().readTree(userInfo).get("id").asLong();
            User user = userRepository.findByKakaoId(kakaoid);

            if (user == null) {
                User newUser = new User();
                newUser.setKakaoId(kakaoid);
                userRepository.save(newUser);
                log.info("새로운 회원 저장 완료: {}", kakaoid);
            } else {
                log.info("이미 가입된 회원입니다: {}", kakaoid);
            }
        } catch (Exception e) {
            log.error("유저 저장 중 오류 발생", e);
            // 롤백 방지를 위해 예외를 밖으로 던지지 않거나, 여기서 처리함
        }
    }

    // [4단계] 토큰 생성
    public String createToken(String userInfo) {
        try {
            String kakaoid = new ObjectMapper().readTree(userInfo).get("id").asText();
            return jwtProvider.createToken("kakao_" + kakaoid);
        } catch (Exception e) {
            throw new RuntimeException("토큰 생성 실패");
        }
    }
}