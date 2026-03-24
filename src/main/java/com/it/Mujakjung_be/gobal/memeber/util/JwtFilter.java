package com.it.Mujakjung_be.gobal.memeber.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService service;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsService service){
        this.jwtUtil= jwtUtil;
        this.service = service;

    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // authorization 해더 가지고 오기
        String authorization = request.getHeader("Authorization");

// 헤더가 없거나 Bearer 로 시작하지 않으면 그냥 통과
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

// Bearer 제거하고 토큰만 추출
        String token = authorization.substring(7);

// 토큰 검증
        if (jwtUtil.validate(token)) {

            // 토큰에서 이메일 추출
            String email = jwtUtil.getEmail(token);

            UserDetails userDetails = service.loadUserByUsername(email);


            // 인증 객체 생성
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            userDetails.getAuthorities()
                    );

            // SecurityContext 에 인증 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터 실행
        filterChain.doFilter(request, response);
    }
}
