package com.it.Mujakjung_be.global.mypage.controller;

import com.it.Mujakjung_be.global.mypage.dto.MyPageDto;
import com.it.Mujakjung_be.global.mypage.entity.MyPageEntity;
import com.it.Mujakjung_be.global.mypage.service.MyPageService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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



}
