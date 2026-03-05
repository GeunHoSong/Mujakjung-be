package com.it.Mujakjung_be.gobal.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    // 비밀 번호 암호 화
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // ✅ CSRF 비활성화
                // - 브라우저 폼 기반(세션)에서 공격 방지를 위한 기능
                // - REST API 테스트/개발에서는 POST 요청이 403으로 막히는 경우가 많아서 끄고 시작하는 경우 많음
                .csrf(csrf -> csrf.disable())

                // ✅ 요청 URL 별 접근 권한 설정
                .authorizeHttpRequests(auth -> auth
                        // 회원가입은 누구나 접근 가능 (로그인 필요 없음)
                        // ⚠️ 앞에 "/" 꼭 붙이기!
                        .requestMatchers("/api/member/join").permitAll()

                        // 그 외 모든 요청은 인증된 사용자만 접근 가능
                        .anyRequest().authenticated()
                )

                // ✅ 기본 로그인 페이지 사용 안 함
                // - 스프링 시큐리티는 기본 로그인 폼(/login)을 자동 제공하는데
                // - 우리는 REST API + (JWT 같은 방식)으로 로그인 구현할 거라서 끔
                .formLogin(form -> form.disable())

                // ✅ HTTP Basic 인증 사용 안 함
                // - Authorization: Basic base64(username:password) 방식
                // - 보통 API 서버에서 JWT 쓸 거면 끄는 편
                .httpBasic(basic -> basic.disable());

        // 설정한 규칙대로 SecurityFilterChain 생성
        return http.build();
    }
}
