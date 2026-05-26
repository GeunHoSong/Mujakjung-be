package com.it.Mujakjung_be.global.member.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtProvider {
    // 이 키는 외부에 절대 노출되면 안 돼! (나중에 application.yml로 옮기는 걸 추천해)
    private final String SECRET_KEY = "Mujakjung-Secret-Key-Must-Be-Long-Enough-To-Be-Secure-1234567890";
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24시간(1일)
    public String createToken(String email) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        return Jwts.builder()
                .setSubject(email)               // 토큰의 주인(이메일)
                .setIssuedAt(now)                // 발행일
                .setExpiration(expiryDate)       // 만료일
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 암호화 방식
                .compact();




    }
}
