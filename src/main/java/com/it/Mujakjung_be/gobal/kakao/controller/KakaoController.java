package com.it.Mujakjung_be.gobal.kakao.controller;

import com.it.Mujakjung_be.gobal.kakao.service.KakaoService;
import jakarta.servlet.http.HttpServletResponse; // 필수 import
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("auth/kakao")
@RequiredArgsConstructor
public class KakaoController {

    private final KakaoService service;

    @GetMapping("/callback")
    public void kakaoCallback(@RequestParam String code, HttpServletResponse response) throws IOException {
        // 1. 인가 코드로 access token 받기
        String accessToken = service.getAccessToken(code);

        // 2. access token으로 사용자 정보 가져오기
        String userInfo = service.getUserInfo(accessToken);

        // 3. DB 저장
        service.saveUser(userInfo);

        // 4. ⭐ 직접 리다이렉트 시키기 (메인 화면으로!)
        // 따로 메서드 만들 필요 없이 response 객체가 가진 기능을 호출만 하면 돼.
        response.sendRedirect("http://localhost:3000");
    }
}