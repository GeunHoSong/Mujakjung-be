package com.it.Mujakjung_be.global.inquiry.controller;

import com.it.Mujakjung_be.global.inquiry.dto.InquiryRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiry")
@CrossOrigin(origins = "http://localhost:5137")
public class InquiryController {
    @PostMapping
    public ResponseEntity<String> submitInquiry(@RequestBody InquiryRequestDto requestDto) {
        System.out.println("1:1 문의 접수됨 - 이메일: " + requestDto.getEmail());
        System.out.println("제목: " + requestDto.getTitle());
        System.out.println("내용: " + requestDto.getContent());

        // TODO: 서비스로직 호출해서 DB 저장 처리

        return ResponseEntity.ok("문의가 성공적으로 접수되었습니다!");
    }

}
