package com.it.Mujakjung_be.global.config;

import com.it.Mujakjung_be.global.member.util.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // 1. [공개 API] 누구나 접근 가능
                        .requestMatchers("/", "/api/member/join", "/api/member/login", "/api/health",
                                "/auth/kakao/**", "/auth/naver/**", "/login/oauth2/code/naver/**",
                                "/favicon.ico", "/error", "/api/travels/**", "/api/search/**").permitAll()

                        // 2. [조회 전용] 게시판과 공지사항 목록은 로그인 없이 가능
                        .requestMatchers(HttpMethod.GET, "/api/board/**", "/api/notice/list").permitAll()

                        // 3. [관리자 권한] 공지사항 쓰기/수정/삭제 (이 규칙이 먼저 와야 인증/인가가 제대로 작동함)
                        .requestMatchers("/api/admin/**", "/api/notice/save", "/api/notice/update/**", "/api/notice/delete/**").hasAuthority("ROLE_ADMIN")

                        // 4. [로그인 필수] 게시판 글쓰기 등 (나머지 게시판 기능)
                        .requestMatchers("/api/board/**").authenticated()

                        // 5. [회원 전용]
                        .requestMatchers("/api/member/**").hasAnyRole("USER", "ADMIN")

                        // 6. 나머지 모든 요청
                        .anyRequest().authenticated()
                )
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(unauthorizedHandler)
                        .accessDeniedHandler(accessDeniedHandler)
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