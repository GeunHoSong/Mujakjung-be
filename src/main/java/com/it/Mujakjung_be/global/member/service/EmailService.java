package com.it.Mujakjung_be.global.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender sender;

    private final Map<String , String> verificationStorage = new ConcurrentHashMap<>();

    public void  sendCodeToEmail(String email){
        String authCode  = String.valueOf(new Random().nextInt(899999) + 100000);

        SimpleMailMessage message =  new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("회원 가입 이메일 인증 번호입니다");

        sender.send(message);

        verificationStorage.put(email, authCode);

    }

    public  boolean verifyCode(String code , String email){
        String storageCode = verificationStorage.get(email);

        if(storageCode != null && storageCode.equals(code)){
            verificationStorage.get(email);
            return true;

        }
        return false;
    }



}
