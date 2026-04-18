package com.it.Mujakjung_be.global.member.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
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

        // 1️⃣ [화이트리스트] 토큰 없이도 들어올 수 있는 주소들은 바로 필터 통과!
        if (path.startsWith("/api/travels") ||
                path.startsWith("/api/admin") ||
                path.equals("/api/member/login") ||
                path.equals("/api/member/join")) {

            filterChain.doFilter(request, response);
            return; // 👈 하이패스 주소는 여기서 종료! 아래 검증 로직 안 타요.
        }

        // 2️⃣ [Authorization 헤더 체크] 헤더에서 토큰을 꺼냄
        String authorization = request.getHeader("Authorization");

        // 헤더가 없거나 "Bearer "로 시작하지 않으면 그냥 통과 (이후 시큐리티가 권한 부족으로 막음)
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // "Bearer " 뒷부분의 토큰 값만 추출
        String token = authorization.substring(7);

        // 3️⃣ [JWT 유효성 검증]
        if (jwtUtil.validate(token)) {
            // 토큰에서 사용자 이메일(아이디) 추출
            String email = jwtUtil.getEmail(token);

            // DB에서 사용자 정보 로드
            UserDetails userDetails = service.loadUserByUsername(email);

            // 4️⃣ [인증 객체 생성 및 저장] 시큐리티가 "이 유저 인증됨!"이라고 인지하게 함
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // 패스워드는 이미 인증됐으니 null
                            userDetails.getAuthorities()
                    );

            // 시큐리티 컨텍스트 홀더에 인증 정보 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 마지막으로 다음 필터로 이동
        filterChain.doFilter(request, response);
    }
}