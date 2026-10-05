package com.it.Mujakjung_be.global.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class SmsService {

    private final Map<String ,String> smsStorage  = new ConcurrentHashMap<>();
    public void sendSmsCode(String phone) {
        String authCode  = String.valueOf(new Random().nextInt(899999) + 100000);
        smsStorage.put(phone, authCode);
    }

    public boolean verify(String phone, String code) {

        String storageCode  = smsStorage.get(phone);

        if (storageCode != null && storageCode.equals(code)){
            smsStorage.remove(phone);
            return true;
        }
        return false;
    }
}
