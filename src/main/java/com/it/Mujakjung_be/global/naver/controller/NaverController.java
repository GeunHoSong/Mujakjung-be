package com.it.Mujakjung_be.global.naver.controller;

import com.it.Mujakjung_be.global.naver.service.NaverService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class NaverController {

    private final NaverService naverService;

    private final String FIXED_STATE = "mujakjungNaverLoginState123";
    private final String CLIENT_ID = "0mtzJI9Tavpqok3pG5Rw";

    @GetMapping("/auth/naver")
    public void naverLogin(HttpServletResponse response) throws IOException {
        // 💡 1. 주소를 반드시 UTF-8로 인코딩해줘야 네이버 세션이 안 깨집니다!
        String redirectUri = URLEncoder.encode("http://localhost:8080/login/oauth2/code/naver", StandardCharsets.UTF_8);

        // 💡 2. 인코딩된 redirectUri를 실어서 보냅니다.
        String apiURL = "https://nid.naver.com/oauth2.0/authorize"
                + "?response_type=code"
                + "&client_id=" + CLIENT_ID
                + "&redirect_uri=" + redirectUri
                + "&state=" + FIXED_STATE;

        System.out.println("=== [최종 검증] 인코딩 완료된 URL -> " + apiURL);
        response.sendRedirect(apiURL);
    }
    @GetMapping("/login/oauth2/code/naver")
    public void naverCallback(@RequestParam String code, @RequestParam String state, HttpServletResponse response) throws IOException {
        try {
            String jwtToken = naverService.processNaverLogin(code, state);

            // 💡 포트를 리액트가 실제로 돌아가고 있는 5173으로 바꾸고, 전용 콜백 경로로 리다이렉트!
            response.sendRedirect("http://localhost:5173/login/oauth2/code/naver?token=" + jwtToken);

        } catch (Exception e) {
            System.err.println("네이버 로그인 중 예외 포착 프론트로 대피합니다: " + e.getMessage());
            // 💡 여기도 포트를 5173으로 교정!
            response.sendRedirect("http://localhost:5173/login?error=naver_session_failed");
        }
    }
}