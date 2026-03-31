package com.it.Mujakjung_be.gobal.kakao.controller;

import com.it.Mujakjung_be.gobal.kakao.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth/kakao")
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService service;

    @GetMapping("/callback")
    public String kakaoCallback(@RequestParam String code){
        // 인가 코드로 access token 받기
        String accessToken = service.getAccessToken(code);

        // access token으로 사용자 정보 가져오기
        String userInfo = service.getUserInfo(accessToken);
        return "카카오 로그인 완료 유저 정보 " + userInfo;
     }
}
