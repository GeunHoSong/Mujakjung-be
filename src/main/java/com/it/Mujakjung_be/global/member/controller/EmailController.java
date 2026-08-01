package com.it.Mujakjung_be.global.member.controller;

import com.it.Mujakjung_be.global.member.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {

    private final EmailService service;

    @PostMapping("/send")
    public ResponseEntity<String> seadVerificationCode(@RequestBody Map<String , String > request){
        String email = request.get("email");
        service.sendCodeToEmail(email);
        return ResponseEntity.ok("인증 번호가 발송 되었습니다");
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifyCode(@RequestBody Map<String , String> request){
        String email  = request.get("email");
        String code = request.get("code");
         boolean isverified = service.verifyCode(email,code);

         return ResponseEntity.ok(isverified);
    }
}
