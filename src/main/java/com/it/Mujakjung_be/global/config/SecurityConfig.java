package com.it.Mujakjung_be.global.config;

import com.it.Mujakjung_be.global.member.util.JwtFilter;
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
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

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

        http
                // 1. CORS 설정 연결 (이게 빠져서 에러 났던 거야!)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 2. CSRF 비활성화
                .csrf(csrf -> csrf.disable())

                // 3. 세션 정책 설정
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // SecurityConfig.java의 filterChain 메서드 내부
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/api/member/join", "/api/member/login").permitAll()
                        .requestMatchers("/auth/kakao/**", "/api/auth/kakao/**").permitAll()
                        .requestMatchers("/api/travels/**").permitAll()

                        // hasRole("ADMIN")은 내부적으로 "ROLE_ADMIN" 권한이 있는지 확인해!
                        .requestMatchers("/api/admin/**").permitAll()
                        .requestMatchers("/api/member/**").hasAnyRole("USER", "ADMIN")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"message\":\"인증이 필요합니다\", \"status\": 401}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"message\": \"권한이 필요 합니다\", \"status\": 403}");
                        })
                );

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration cs = new CorsConfiguration();

        // 1. 모든 도메인(Origin) 허용
        // "*"은 모든 곳에서 접속 가능하다는 뜻이야!
        cs.setAllowedOriginPatterns(Arrays.asList("*"));

        // 2. 허용할 HTTP 메서드 설정
        // GET, POST 등 우리가 사용할 기능들을 허락해주는 거야. (OPTIONS 오타 수정 완료!)
        cs.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // 3. 허용할 헤더 설정
        // 클라이언트가 보낼 수 있는 모든 헤더 정보를 다 받겠다는 뜻!
        cs.setAllowedHeaders(Arrays.asList("*"));

        // 4. 자격 증명 허용
        // 쿠키나 인증 정보를 주고받을 수 있게 true로 설정해줘.
        cs.setAllowCredentials(true);

        // 5. 설정을 실제 경로에 적용
        // "/**"는 서버의 모든 주소에 이 설정을 다 적용하겠다는 의미야.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cs); // registerCorsConfiguration 만든 cors 규칙을 적용할 지 등록 하는 결제 도장


        return source;
    }
}