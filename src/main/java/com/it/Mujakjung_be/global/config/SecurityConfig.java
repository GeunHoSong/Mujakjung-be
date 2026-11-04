
package com.it.Mujakjung_be.global.config;

import com.it.Mujakjung_be.global.member.util.JwtFilter;
import com.it.Mujakjung_be.global.oauth2.OAth2SuccessHandler;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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

/**
 * Spring Security 설정 클래스
 *
 * 주요 역할
 * 1. JWT 인증 설정
 * 2. 관리자, 상담사, 회원 접근 권한 구분
 * 3. OAuth2 소셜 로그인 설정
 * 4. CORS 설정
 * 5. 비밀번호 암호화 설정
 */
@Configuration
@EnableWebSecurity(debug = false)
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    // JWT 토큰을 검사하는 필터
    private final JwtFilter jwtFilter;

    // 권한이 부족한 경우 403 처리
    private final AccessDeniedHandler accessDeniedHandler;

    // 인증되지 않은 경우 401 처리
    private final UnauthorizedHandler unauthorizedHandler;

    // OAuth2 소셜 로그인 성공 후 처리
    private final OAth2SuccessHandler oAuth2SuccessHandler;


    /**
     * 1. 비밀번호 암호화 설정
     *
     * BCryptPasswordEncoder:
     * 회원 비밀번호를 안전하게 해시 처리
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 2. HTTP 보안 설정
     *
     * SecurityFilterChain:
     * 요청 URL별 인증 및 권한을 검사
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // ① React와 Spring Boot 간 CORS 설정
                .cors(cors ->
                        cors.configurationSource(corsConfigurationSource())
                )

                // ② REST API에서 CSRF 보호 비활성화
                // JWT 기반 API 요청을 전제로 한 설정
                .csrf(csrf -> csrf.disable())

                // ③ JWT 기반 인증이므로 세션을 생성하지 않음
                .sessionManagement(sm ->
                        sm.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // ④ URL별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth

                        /*
                         * [공개 API]
                         * 로그인하지 않은 사용자도 접근 가능
                         */
                        .requestMatchers(
                                "/",
                                "/api/member/join",
                                "/api/member/login",
                                "/api/health",

                                // WebSocket 연결 경로
                                "/ws/chat",

                                // 소셜 로그인
                                "/auth/kakao/**",
                                "/auth/naver",
                                "/oauth2/**",
                                "/login/**",

                                // 검색 및 공개 기능
                                "/api/search/**",
                                "/api/comment/**",
                                "/api/member/display/**",
                                "/api/email/**",
                                "/api/inquiry",

                                // 기존 AI 상담
                                "/api/chat",

                                // 기존 공개 기능
                                "/api/bookmarks/**",
                                "/api/member/check-nickname",

                                "/error",
                                "/favicon.ico",
                                "/.well-known/**"
                        ).permitAll()

                        /*
                         * [여행 상품 공개 조회]
                         *
                         * GET 요청은 누구나 가능
                         * POST, PUT, PATCH, DELETE는 별도 검사
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/travels/**"
                        ).permitAll()

                        /*
                         * [여행 상품 변경]
                         *
                         * 등록, 수정, 삭제는 관리자만 가능
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/travels/**"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/travels/**"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/travels/**"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/travels/**"
                        ).hasAuthority("ROLE_ADMIN")

                        /*
                         * [게시판 및 공지사항 조회]
                         *
                         * GET 요청은 로그인 없이 접근 가능
                         */
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/board/**",
                                "/api/notice/**"
                        ).permitAll()

                        /*
                         * [관리자 권한]
                         *
                         * ROLE_ADMIN만 접근 가능
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/notice/save"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/notice/update/**"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/notice/delete/**"
                        ).hasAuthority("ROLE_ADMIN")

                        .requestMatchers(
                                "/api/admin/**"
                        ).hasAuthority("ROLE_ADMIN")

                        /*
                         * [상담사 권한]
                         *
                         * ROLE_AGENT만 접근 가능
                         * 상담사 전용 REST API에 적용
                         */
                        .requestMatchers(
                                "/api/agent/**"
                        ).hasAuthority("ROLE_AGENT")

                        /*
                         * [일반 회원 기능]
                         *
                         * 로그인한 사용자만 접근 가능
                         */
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/board/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/board/**"
                        ).authenticated()

                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/board/**"
                        ).authenticated()

                        .requestMatchers(
                                "/api/mypage/**"
                        ).authenticated()

                        /*
                         * [기타 요청]
                         *
                         * 위 조건에 해당하지 않으면
                         * 로그인한 사용자만 접근 가능
                         */
                        .anyRequest().authenticated()
                )

                // ⑤ 기본 로그인 폼 비활성화
                .formLogin(form -> form.disable())

                // ⑥ HTTP Basic 인증 비활성화
                .httpBasic(basic -> basic.disable())

                // ⑦ OAuth2 소셜 로그인 설정
                .oauth2Login(oauth2 ->
                        oauth2.successHandler(oAuth2SuccessHandler)
                )

                // ⑧ JWT 필터 등록
                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                )

                // ⑨ 인증 및 권한 예외 처리
                .exceptionHandling(ex -> ex

                        // 인증되지 않은 사용자: 401
                        .authenticationEntryPoint(
                                unauthorizedHandler
                        )

                        // 권한이 없는 사용자: 403
                        .accessDeniedHandler(
                                accessDeniedHandler
                        )
                );

        return http.build();
    }


    /**
     * 3. CORS 설정
     *
     * React 개발 서버와 Spring Boot 서버 간
     * HTTP 요청을 허용하기 위한 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // ① 허용할 React 개발 서버 주소
        config.setAllowedOriginPatterns(
                Arrays.asList("http://localhost:5173")
        );

        // ② 허용할 HTTP 메서드
        config.setAllowedMethods(
                Arrays.asList(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );

        // ③ 허용할 요청 헤더
        config.setAllowedHeaders(
                Arrays.asList(
                        "Authorization",
                        "Content-Type",
                        "Accept"
                )
        );

        // ④ 자격증명 포함 요청 허용
        config.setAllowCredentials(true);

        // ⑤ 모든 API 경로에 CORS 설정 적용
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
