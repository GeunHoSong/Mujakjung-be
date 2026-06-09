package com.it.Mujakjung_be.global.config;

import com.it.Mujakjung_be.global.member.util.JwtFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
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
@Slf4j
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
                // 1. 가장 먼저 CORS 설정 적용
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 2. 요청 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/api/member/join", "/api/member/login", "/api/health",
                                "/auth/kakao/**", "/auth/naver/**", "/login/oauth2/code/naver/**",
                                "/favicon.ico", "/error", "/api/travels/**", "/api/search/**").permitAll()
                        .requestMatchers("/api/admin/**", "/api/notice/save").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/member/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable())

                // 3. JwtFilter를 UsernamePasswordAuthenticationFilter 이전에 실행
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

        // 1. '*' 대신 구체적인 패턴을 사용하거나, 패턴 리스트를 명시해야 함
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:5173"));

        // 2. 허용할 메서드들
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 3. 헤더 설정 (Authorization은 토큰 때문에 반드시 필요)
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));

        // 4. 인증 정보(쿠키, 토큰 등) 허용
        config.setAllowCredentials(true);

        // 5. 서버에 설정 등록
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        // 💡여기가 찐 범인 검거 장소! 네이버 콜백 주소들이 JwtFilter를 아예 타지 않도록 확실하게 추가해 줘야 해!
//        return (web -> web.ignoring().requestMatchers(
//                "/auth/kakao", "/auth/kakao/**",
//                "/auth/naver", "/auth/naver/**",
//                "/login/oauth2/code/naver",
//                "/login/oauth2/code/naver/**",
//                "favicon.ico", "/.well-known/**"
//        ));
//    }
}