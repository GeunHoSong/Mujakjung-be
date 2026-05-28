package com.it.Mujakjung_be.global.member.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j // 권한 부여
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService service;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsService service){
        this.jwtUtil = jwtUtil;
        this.service = service;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // ⭐ [여기 추가!] 카카오 콜백 경로 예외 처리
        String path = request.getRequestURI();
        if (path.startsWith("/auth/kakao")) {
            filterChain.doFilter(request, response);
            return; // 여기서 필터 종료 (검증 로직 안 탐)
        }

        String authorization = request.getHeader("Authorization");

        // (이하 기존 코드 유지)
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            // 토큰에서 유저 정보
            String email = jwtUtil.getEmail(token);
            // db 에서 유저 정보 로드 (이메일 기반 ㅏ으로 조회 하도록 세팅)
            UserDetails userDetails = service.loadUserByUsername(email);
            // 인증 객체 ㅏ생성밎 새션 저장
            UsernamePasswordAuthenticationToken u = new UsernamePasswordAuthenticationToken(userDetails, null , userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(u);
        }

        filterChain.doFilter(request, response);
    }


}