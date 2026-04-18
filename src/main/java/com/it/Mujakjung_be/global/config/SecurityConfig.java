package com.it.Mujakjung_be.global.config;

import com.it.Mujakjung_be.global.member.util.JwtFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

// 1. 설정 클래스 선언부
@Configuration        // 이 클래스를 스프링 설정으로 사용함
@EnableWebSecurity    // 스프링 시큐리티 기능을 활성화함 (손코딩 시 강조하기 좋아!)
@RequiredArgsConstructor // final이 붙은 필드(JwtFilter)의 생성자를 자동으로 만듦
@Slf4j                // 로그 출력을 위한 어노테이션
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AccessDeniedHandler accessDeniedHandler;
    private final UnauthorizedHandler unauthorizedHandler;

    // 2. 암호화 빈 등록
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 3. HTTP 보안 설정 (가장 중요한 부분)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS/CSRF/Session 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // URL 권한 제어
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/api/member/join", "/api/member/login").permitAll()
                        .requestMatchers("/api/travels/**", "/api/auth/kakao/**").permitAll()
                        .requestMatchers("/api/member/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )

                // 로그인 방식 비활성화 및 필터 추가
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // 예외 처리 (EntryPoint, AccessDeniedHandler)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(unauthorizedHandler) // 따로 메서드로 빼면 손코딩이 깔끔해져!
                        .accessDeniedHandler(accessDeniedHandler)
                );

        return http.build();
    }

    // 4. CORS 설정 Bean
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(Arrays.asList("*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}