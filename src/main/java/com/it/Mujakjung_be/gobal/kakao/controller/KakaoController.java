package com.it.Mujakjung_be.gobal.kakao.controller;

import com.it.Mujakjung_be.gobal.kakao.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/kakao")
@RequiredArgsConstructor
public class KakaoController {
    private final KakaoService service;

    @GetMapping("/callback")
    public String kakaoCallback(@RequestParam String code){
        // 코드 가 잘 왔는지 찍어 보자
//        System.out.println("카카오 에서 보낸준 코드" + code);
//        return "백엔드 연결 성공 ! 받은 코드 " +code;
        // 프론트에서 받은 인가 코드로 '엑세스 토큰 '을 받아와
        String accessToken = service.getAccessToken(code);
        // 받은 토큰 으로 카카오 우저 정보 가져와 (지금은 로그로 확인 하고 , 나중에 db  저장 로직을 넣을때)
        String userInfo = service.getAccessToken(accessToken);
        return "카카오 로그인 완료 유저 정보 " + userInfo;
     }
}
