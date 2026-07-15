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
        String path = request.getRequestURI();

        // [인증 제외 경로] 로그인을 하지 않아도 접근해야 하는 API들은 여기서 바로 필터 통과
        if (path.startsWith("/auth/") || path.startsWith("/api/member/login") || path.startsWith("/api/member/join") || path.startsWith("/api/board/")) {
            filterChain.doFilter(request, response);
            return;
        }

        // [토큰 확인] 프론트엔드에서 보낸 Authorization 헤더를 가져옴
        String authorization = request.getHeader("Authorization");
        log.info("▶ [JwtFilter] 요청 경로: {}, 헤더(Authorization): {}", path, authorization);

        // [토큰 검증 프로세스]
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7); // "Bearer " 부분 제거하고 순수 토큰만 추출

            try {
                if (jwtUtil.validateToken(token)) { // 토큰이 유효한지 검증
                    String email = jwtUtil.getEmail(token);
                    UserDetails userDetails = service.loadUserByUsername(email);

                    // 인증 객체를 생성하여 SecurityContext에 저장 (이게 있어야 컨트롤러에서 사용자 정보를 알 수 있음)
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    log.info("▶ [JwtFilter] 인증 성공! 사용자: {}", email);
                } else {
                    log.warn("▶ [JwtFilter] 토큰 검증 실패!");
                }
            } catch (Exception e) {
                log.error("▶ [JwtFilter] 에러 발생: {}", e.getMessage());
            }
        } else {
            // [결정적 힌트] 여기서 로그가 뜨면 프론트엔드에서 토큰을 아예 안 보낸 것임!
            log.warn("▶ [JwtFilter] 헤더에 토큰이 없습니다.");
        }

        filterChain.doFilter(request, response);
    }


}