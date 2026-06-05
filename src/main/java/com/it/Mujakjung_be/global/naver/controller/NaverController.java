package com.it.Mujakjung_be.global.naver.controller;

import com.it.Mujakjung_be.global.naver.service.NaverService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
// 💡 [핵심] 상단 @RequestMapping("/auth")를 지워서 하위 메서드 주소들이 꼬이지 않게 독립시킵니다!
public class NaverController {

    private final NaverService service;

    /**
     * 1. 리액트 프론트에서 최초로 로그인 버튼 누를 때 요청하는 주소
     * 실제 주소: http://localhost:8080/auth/naver
     */

    @GetMapping("/auth/naver")
    public void naverLogin(
            HttpServletResponse response,
            HttpSession session) throws IOException {

        String clientId = "0mtzJI9Tavpqok3pG5Rw";
        String redirectUri =
                URLEncoder.encode(
                        "http://localhost:8080/login/oauth2/code/naver",
                        "UTF-8");

        String state = UUID.randomUUID().toString();

        // 추가
        session.setAttribute("NAVER_STATE", state);

        String apiURL =
                "https://nid.naver.com/oauth2.0/authorize?response_type=code"
                        + "&client_id=" + clientId
                        + "&redirect_uri=" + redirectUri
                        + "&state=" + state;

        response.sendRedirect(apiURL);
    }

    /**
     * 2. 네이버가 코드 들고 돌아와서 404 에러 뿜게 만들었던 바로 그 주소!
     * 실제 주소: http://localhost:8080/login/oauth2/code/naver
     */
    @GetMapping("/login/oauth2/code/naver")
    public String naverCallback(@RequestParam String code , @RequestParam String state){

        // 이제 주소가 완벽히 일치하므로 이 메서드가 정상적으로 낚아챕니다!
        String jwtToken = service.processNaverLogin(code, state);

        return jwtToken;
    }
}