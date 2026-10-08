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

@Configuration
@EnableWebSecurity(debug = false) // Spring Security 활성화
@RequiredArgsConstructor
@EnableMethodSecurity // @PreAuthorize 등의 메서드 단위 보안 어노테이션 사용 활성화
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    private final UnauthorizedHandler unauthorizedHandler;
    private final OAth2SuccessHandler oAuth2SuccessHandler; // 소셜 로그인 성공 시 처리를 담당하는 핸들러

    /**
     * 비밀번호 암호화를 위한 BCrypt 인코더 빈 등록
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * HTTP 보안 필터 체인 설정 (핵심 시큐리티 설정)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. CORS 설정 적용 (하단의 corsConfigurationSource 빈을 참조)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. CSRF 보안 비활성화 (REST API 서버이므로 상태를 유지하지 않아 보통 비활성화함)
                .csrf(csrf -> csrf.disable())

                // 3. 세션 관리 정책 설정: JWT 토큰 기반 인증을 사용하므로 세션을 생성하지 않음 (STATELESS)
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. URL별 접근 권한 관리 (Authorization)
                .authorizeHttpRequests(auth -> auth
                        // ① [공개 경로] 로그인, 회원가입, 헬스체크, 소셜 로그인, 각종 조회 API 등 누구나 접근 가능
                        .requestMatchers("/", "/api/member/join", "/api/member/login", "/api/health",
                                "/auth/kakao/**", "/auth/naver", "/oauth2/**", "/login/**", "/api/travels/**",
                                "/api/search/**", "/api/comment/**", "/api/member/display/**",
                                "/error", "/api/email/**", "/api/inquiry", "/api/member/check-nickname",
                                "/favicon.ico", "/.well-known/**").permitAll()

                        // ② [조회 권한] 게시판 및 공지사항 목록 조회는 비회원도 가능 (GET 요청만 허용)
                        .requestMatchers(HttpMethod.GET, "/api/board/**", "/api/notice/**").permitAll()

                        // ③ [관리자 권한] 공지사항 등록/수정/삭제 및 관리자 전용 API는 ROLE_ADMIN 권한 소유자만 접근 가능
                        .requestMatchers(HttpMethod.POST, "/api/notice/save").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/notice/update/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/notice/delete/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN")

                        // ④ [회원 권한] 일반 게시판 글쓰기/수정/삭제 및 마이페이지는 인증된 회원만 접근 가능
                        .requestMatchers(HttpMethod.POST, "/api/board/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/board/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/board/**").authenticated()
                        .requestMatchers("/api/mypage/**").authenticated()

                        // ⑤ 나머지 모든 요청은 반드시 인증(로그인)을 거쳐야 접근 가능
                        .anyRequest().authenticated()
                )

                // 5. 기본 form 로그인 및 http basic 인증 비활성화 (JWT 및 OAuth2를 사용하므로)
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable())

                // 6. OAuth2 로그인 설정 (로그인 성공 시 후속 처리를 할 커스텀 핸들러 연결)
                .oauth2Login(oauth2 -> oauth2.successHandler(oAuth2SuccessHandler))

                // 7. JWT 인증 필터 추가: UsernamePasswordAuthenticationFilter보다 앞서서 실행되도록 설정
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // 8. 예외 처리 핸들러 설정 (인증되지 않은 사용자 또는 권한이 부족한 사용자의 접근 실패 시 처리)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(unauthorizedHandler) // 401 Unauthorized 처리
                        .accessDeniedHandler(accessDeniedHandler)       // 403 Forbidden 처리
                );

        return http.build();
    }

    /**
     * CORS(Cross-Origin Resource Sharing) 설정
     * 프론트엔드(예: Vite + React 개발 서버인 http://localhost:5173)와의 통신 허용
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 허용할 프론트엔드 오리진 설정
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:5173"));

        // 허용할 HTTP 메서드 설정
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 허용할 HTTP 헤더 설정 (Authorization: 토큰 전달용, Content-Type, Accept 등)
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));

        // 자격증명(쿠키, 인증 헤더 등)을 포함한 요청 허용 여부
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 모든 경로에 대해 위 CORS 설정 적용
        return source;
    }
}