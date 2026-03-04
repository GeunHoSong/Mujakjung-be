package com.it.Mujakjung_be.gobal.memeber.service;

import com.it.Mujakjung_be.gobal.memeber.entity.MemberEntity;
import com.it.Mujakjung_be.gobal.memeber.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor // final 필드 생성자 자동 생성 , 의존성 주입
public class MemberService {

    private final MemberRepository repository;

    public void save(String email, String password){
        // 이메일 중복 검사
        if(repository.existsByEmail(email)){
            throw new RuntimeException("이미 존재 하는 이메일 입니다");
        }
        MemberEntity entity = new MemberEntity();
        entity.setEmail(email);
        entity.setPassword(password);

        repository.save(entity);
    }




}
