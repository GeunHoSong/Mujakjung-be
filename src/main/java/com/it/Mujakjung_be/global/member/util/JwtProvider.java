package com.it.Mujakjung_be.global.member.util; // 1. 패키지 선언은 맨 윗줄에!

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;
import java.util.Date;

@Component
public class JwtProvider {

    // 32자 이상인 안전한 키
    private final String SECRET_KEY_STRING = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";

    // ✅ 아래처럼 직접 getBytes()를 사용하는 게 훨씬 에러가 적어!
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes());
    }

    public String createToken(String email) {
        return Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 86400000)) // 24시간
                .signWith(getSigningKey())
                .compact();
    }
}