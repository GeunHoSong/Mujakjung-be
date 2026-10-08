package com.it.Mujakjung_be.global.inquiry.controller;

import com.it.Mujakjung_be.global.inquiry.dto.InquiryRequestDto;
import com.it.Mujakjung_be.global.inquiry.service.InquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inquiry")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173") // 프론트 포트가 5173이라면 여기도 확인!
public class InquiryController {

    private final InquiryService service;

    @PostMapping // <-- 이 어노테이션을 꼭 붙여줘야 해!
    public ResponseEntity<String> submitInquiry(@RequestBody InquiryRequestDto dto){
        try{
            service.saveInquiry(dto);
            return ResponseEntity.ok("문의가 성공적으로 접수 되어 DB에 저장 되었습니다");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("문의 접수중 오류가 발생 했습니다: " + e.getMessage());
        }
    }
}