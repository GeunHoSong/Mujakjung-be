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
        // 💡 이 로직이 없으면 브라우저는 CORS 에러를 뿜게 돼!
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 인증이 필요 없는 경로 통합 관리
        String path = request.getRequestURI();
        if (path.startsWith("/auth/") || path.startsWith("/api/member/login") || path.startsWith("/api/member/join")) {
            filterChain.doFilter(request, response);
            return;
        }
        String authorization = request.getHeader("Authorization");

        try {
            if (authorization != null && authorization.startsWith("Bearer ")) {
                String token = authorization.substring(7);

                // ⭐ 여기서 만료된 토큰이면 예외가 발생하는데, catch문으로 이동함
                String email = jwtUtil.getEmail(token);

                UserDetails userDetails = service.loadUserByUsername(email);
                log.info("email={}", email);
                log.info("authorities={}", userDetails.getAuthorities());
                UsernamePasswordAuthenticationToken u = new UsernamePasswordAuthenticationToken(userDetails, null , userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(u);
            }
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            log.warn("토큰이 만료되었습니다: {}", e.getMessage());
            // 만료된 경우 인증을 설정하지 않고 그냥 통과 (필요하다면 여기서 401 응답을 내려줄 수도 있음)
        } catch (Exception e) {
            log.error("토큰 검증 중 오류 발생: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

}