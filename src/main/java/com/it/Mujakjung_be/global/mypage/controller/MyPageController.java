package com.it.Mujakjung_be.global.mypage.controller;

import com.it.Mujakjung_be.global.member.util.JwtFilter;
import com.it.Mujakjung_be.global.member.util.JwtUtil;
import com.it.Mujakjung_be.global.mypage.dto.MyPageDto;
import com.it.Mujakjung_be.global.mypage.service.MyPageService;
import com.it.Mujakjung_be.global.naver.dto.NaverDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/member")
public class MyPageController {

    private final MyPageService myPageService;
    private final JwtUtil util;

    // 1. 마이페이지 정보 조회
    @PostMapping("/mypage")
    public ResponseEntity<?> getMyPage(@RequestHeader(value = "Authorization", required = false) String tokenHeader){
        if(tokenHeader == null || !tokenHeader.startsWith(("Bearer"))){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("토큰이 없습니다");

        }
        String token = tokenHeader.substring(7);
        String email  = util.getEmail(token);

        MyPageDto dto  = myPageService.getMyPageByEmail(email);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/updates")
    public ResponseEntity<?> updateMyPage(@RequestHeader(value = "Authorization" , required = false) String tokenHeader, @ModelAttribute MyPageDto dto ,
                                          @RequestParam(value = "file", required = false) MultipartFile file){
        if(tokenHeader == null || !tokenHeader.startsWith("Bearer")){
            log.error("토큰 누락 또는 헤더 오류");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 이용이 가능 합니다");
        }

        String token  = tokenHeader.substring(7);

        // getEmail (token) 매서드 사용
        if(!util.validateToken(token)){
            log.error("유호 하지 않은 토큰 입니다 ");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효 하지 않은 토큰 입니다");
        }

        String email = util.getEmail(token);

        myPageService.updateMypage(email , dto , file);
        return ResponseEntity.ok("프로필 수정");

    }
}