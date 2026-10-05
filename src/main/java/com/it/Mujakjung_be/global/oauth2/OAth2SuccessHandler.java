package com.it.Mujakjung_be.global.oauth2;

import com.it.Mujakjung_be.global.member.util.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        // 💡 참고: 소셜 로그인 제공자(구글, 카카오 등)에 따라 속성(Attribute) 키 값이 다를 수 있으니
        // 나중에 로그 찍히는 걸 보고 이메일/이름 키 값을 맞추면 돼!
        String email = (String) oAuth2User.getAttributes().get("email");
        String name = (String) oAuth2User.getAttributes().get("name");

        log.info("▶ [OAuth2SuccessHandler] 소셜 로그인 성공! 이메일: {}, 이름: {}", email, name);

        // 1. 로그인 성공한 유저의 이메일 기반으로 JWT 토큰 생성
        String token = jwtUtil.createToken(email);

        // 2. 프론트엔드 콜백 페이지로 토큰을 쿼리 파라미터에 담아서 리다이렉트
        String redirectUrl = "http://localhost:5173/login/oauth2/code/google?token=" + token;

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}