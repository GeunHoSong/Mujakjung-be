package com.it.Mujakjung_be.global.member.controller;

import com.it.Mujakjung_be.global.member.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sms")
public class SmsController {

    private final SmsService service;


    @PostMapping("/send")
    public ResponseEntity<String> sendSmsCode(@RequestBody Map<String , String> request){
        String phone  = request.get("phone");
        service.sendSmsCode(phone);
        return ResponseEntity.ok("인증 번호가 발송을 되었습니다");
    }

    @PostMapping("/verify")
    public ResponseEntity<Boolean> verifySmsCode(@RequestBody Map<String ,String> request){
        String phone = request.get("phone");
        String code = request.get("code");
        boolean is  = service.verify(phone, code);

        return ResponseEntity.ok(is);

    }
}
