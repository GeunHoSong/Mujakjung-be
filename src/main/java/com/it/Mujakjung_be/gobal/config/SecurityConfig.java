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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter; // 우리가 만든 JWT 필터 주입

    /**
     * 비밀번호 암호화 빈(Bean) 등록
     * BCrypt 해시 함수를 사용해서 비밀번호를 안전하게 암호화함
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 시큐리티의 핵심 설정 (필터 체인)
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 1. CSRF 보안 비활성화 (REST API는 보통 세션을 안 써서 꺼둠)
        http.csrf(csrf -> csrf.disable())

                // 2. 세션 정책 설정: 세션을 만들지도 않고 사용하지도 않음 (JWT 방식의 핵심!)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // 3. URL별 접근 권한 제어
                .authorizeHttpRequests(auth -> auth
                        // 회원가입과 로그인은 아무나 접근 가능(permitAll)
                        .requestMatchers("/","/api/member/join", "/api/member/login").permitAll()
                        .requestMatchers("/auth/kakao/**","/api/auth/kakao/**").permitAll()
                        // /api/admin/으로 시작하는 건 ADMIN 권한만 가능
                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN")
                        // /api/member/로 시작하는 건 USER나 ADMIN 권한이 있어야 함
                        .requestMatchers("/api/member/**").hasAnyRole("USER", "ADMIN")
                        // 그 외의 모든 요청은 반드시 인증(로그인)을 해야 함
                        .anyRequest().authenticated()
                )

                // 4. 불필요한 기본 기능 끄기
                .formLogin(form -> form.disable()) // 기본 로그인 폼 안 씀
                .httpBasic(basic -> basic.disable()) // 기본 HTTP 인증 방식 안 씀

                // 5. JWT 필터 위치 설정
                // UsernamePasswordAuthenticationFilter(기본 로그인 필터)보다 먼저 내 필터를 실행해라!
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // 6. 에러 처리 (로그인 실패나 권한 부족 시 응답 설정)
                .exceptionHandling(ex -> ex
                        // 인증 실패 시 (401 에러)
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"message\":\"인증이 필요합니다\", \"status\": 401}");
                        })
                        // 권한 부족 시 (403 에러)
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"message\": \"권한이 필요 합니다\", \"status\": 403}");
                        })
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration cs = new CorsConfiguration();


    }
}