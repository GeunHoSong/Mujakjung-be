package com.it.Mujakjung_be.global.memeber.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

    // JWT 서명에 사용할 SecretKey
    private final SecretKey secretKey;

    // 토큰 만료 시간
    private final long expiration;

    // application.yml 에서 값 가져오기
    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration
    ) {
        // 문자열 secret을 Key 객체로 변환
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());

        // 토큰 만료 시간 저장
        this.expiration = expiration;
    }

    /**
     * JWT 토큰 생성
     */
    public String createToken(String email) {

        // 현재 시간
        Date now = new Date();

        // 만료 시간
        Date expireDate = new Date(now.getTime() + expiration);

        // JWT 생성
        return Jwts.builder()

                // 토큰의 주인 (로그인 사용자)
                .subject(email)

                // 발급 시간
                .issuedAt(now)

                // 만료 시간
                .expiration(expireDate)

                // secretKey 로 서명
                .signWith(secretKey)

                // 토큰 문자열 생성
                .compact();
    }

    /**
     * 토큰에서 이메일 추출
     */
    public String getEmail(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * 토큰 만료 여부 확인
     */
    public boolean isExpired(String token) {
        return getClaims(token)
                .getExpiration()
                .before(new Date());
    }

    /**
     * 토큰 검증
     */
    public boolean validate(String token) {

        try {
            return !isExpired(token);

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 토큰 내부 정보 가져오기
     */
    private Claims getClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}