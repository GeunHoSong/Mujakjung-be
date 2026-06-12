package com.it.Mujakjung_be.global.config;

import com.it.Mujakjung_be.global.member.util.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // HttpMethod 사용을 위해 추가
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    private final UnauthorizedHandler unauthorizedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable()) // API 서버이므로 CSRF 비활성화
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // JWT 사용을 위한 세션 비활성화

                .authorizeHttpRequests(auth -> auth
                        // 1. [완전 공개] 로그인 없이 누구나 접근 가능
                        .requestMatchers("/", "/api/member/join", "/api/member/login", "/api/health",
                                "/auth/kakao/**", "/auth/naver/**", "/login/oauth2/code/naver/**",
                                "/favicon.ico", "/error", "/api/travels/**", "/api/search/**").permitAll()

                        // 2. [게시판 조회만] 로그인이 필요 없는 읽기 전용
                        .requestMatchers(HttpMethod.GET, "/api/board/**").permitAll()

                        // 3. [게시판 작성/수정/삭제] 로그인 필수
                        .requestMatchers("/api/board/**").authenticated()

                        // 4. [관리자 전용] 공지사항 관련 모든 작업 및 관리자 API
                        .requestMatchers("/api/admin/**", "/api/notice/save", "/api/notice/update/**", "/api/notice/delete/**").hasAuthority("ROLE_ADMIN")

                        // 5. [회원 전용] 로그인한 사용자 접근 가능
                        .requestMatchers("/api/member/**").hasAnyRole("USER", "ADMIN")

                        // 6. 나머지 모든 요청은 로그인 필요
                        .anyRequest().authenticated()
                )
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class) // JWT 인증 필터 적용
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(unauthorizedHandler) // 로그인 안 된 경우 처리
                        .accessDeniedHandler(accessDeniedHandler)     // 권한 없는 경우 처리
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:5173"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        config.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}