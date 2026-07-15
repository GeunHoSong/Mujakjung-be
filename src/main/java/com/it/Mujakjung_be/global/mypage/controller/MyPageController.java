package com.it.Mujakjung_be.global.mypage.controller;

import com.it.Mujakjung_be.global.mypage.dto.MyPageDto;
import com.it.Mujakjung_be.global.mypage.entity.MyPageEntity;
import com.it.Mujakjung_be.global.mypage.service.MyPageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/api/mypage")
public class MyPageController {
    private final MyPageService service;

    @GetMapping("/{id}")
    public ResponseEntity<MyPageDto> getMyPage(@PathVariable  Long id){
        MyPageDto mypage = service.getMypage(id);
        return ResponseEntity.ok(mypage);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMypage(
            @PathVariable Long id,
            @RequestBody MyPageDto dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 1. 여기서 userDetails가 null이라면 인증이 안 된 것
        if (userDetails == null) {
            log.error("▶ 인증 실패: 사용자를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 후 이용 가능합니다.");
        }

        // 2. 인증 성공 시 비즈니스 로직 수행
        service.updateMypage(id, dto);
        return ResponseEntity.ok("성공");
    }



}
