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

        String authorization = request.getHeader("Authorization");

        // 1. 토큰이 있는 경우에만 검증 로직을 타게 함
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);
            try {
                if (jwtUtil.validate(token)) {
                    String email = jwtUtil.getEmail(token);
                    UserDetails userDetails = service.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                log.error("JWT 검증 에러: {}", e.getMessage());
                // 에러가 나도 filterChain.doFilter를 타야 하므로 여기서 멈추지 않음
            }
        }

        // 2. ⭐️ 토큰이 있든 없든, 에러가 나든 안 나든 "무조건 딱 한 번" 다음 필터로 넘김
        filterChain.doFilter(request, response);
    }


}