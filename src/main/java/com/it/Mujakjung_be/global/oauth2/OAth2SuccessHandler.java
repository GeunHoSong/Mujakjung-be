package com.it.Mujakjung_be.global.oauth2;

import com.it.Mujakjung_be.global.member.entity.MemberEntity;
import com.it.Mujakjung_be.global.member.repository.MemberRepository;
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
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = null;
        String name = null;

        // 1. 구글 로그인 구조 확인
        if (attributes.containsKey("email")) {
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
        }
        // 2. 카카오 로그인 구조 확인 (kakao_account)
        else if (attributes.containsKey("kakao_account")) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakaoAccount.get("email");

            Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
            if (properties != null) {
                name = (String) properties.get("nickname");
            }
        }
        // 3. 네이버 로그인 구조 확인 (response)
        else if (attributes.containsKey("response")) {
            Map<String, Object> responseMap = (Map<String, Object>) attributes.get("response");
            email = (String) responseMap.get("email");
            name = (String) responseMap.get("name");
        }

        log.info("▶ [OAuth2SuccessHandler] 파싱된 이메일: {}, 이름: {}", email, name);

        if (email == null) {
            log.error("▶ [OAuth2SuccessHandler] 소셜 로그인 실패: 이메일 정보를 가져오지 못했습니다.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "소셜 로그인에서 이메일 정보를 제공하지 않았습니다.");
            return;
        }

        // DB에서 회원의 최신 권한 조회
        MemberEntity member = memberRepository.findByEmail(email).orElse(null);
        String role = (member != null && member.getRole() != null) ? member.getRole().name() : "USER";

        // JWT 토큰 생성
        String token = jwtUtil.createToken(email);

        // 한글 이름으로 인한 헤더 인코딩 에러 방지를 위해 name은 제외하고 token과 role만 전달
        String redirectUrl = UriComponentsBuilder.fromUriString("http://localhost:5173/login")
                .queryParam("token", token)
                .queryParam("role", role)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }
}