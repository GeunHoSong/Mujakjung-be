package com.it.Mujakjung_be.gobal.config;

import com.it.Mujakjung_be.gobal.memeber.util.JwtFilter;
import com.it.Mujakjung_be.gobal.memeber.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    // 비밀 번호 암호 화
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        /**
         * CSRF 보호 기능 비활성화
         *
         * - 기본적으로 Spring Security는 CSRF 보호 기능이 켜져 있음
         * - REST API 서버에서는 보통 사용하지 않기 때문에 꺼줌
         * - 안 끄면 POST 요청이 403으로 막히는 경우 많음
         */
        http.csrf(csrf -> csrf.disable())

                /* 세션을 사용하지 않음 (JWT 방식) */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                /* URL 별 접근 권한 설정 */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/member/join", "/api/member/login").permitAll().requestMatchers("/api/admin/**").hasAnyRole("ADMIN").requestMatchers("/api/member/**").hasAnyRole("USER", "ADMIN")

                        /* 나머지는 인증 필요 */
                        .anyRequest().authenticated()

                )
                /*기본 로그인 페이지 비활성화*/
                .formLogin(form-> form.disable())
                /*http Basic  인증 비 활성화 */
                .httpBasic(basic -> basic.disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
    }
}
