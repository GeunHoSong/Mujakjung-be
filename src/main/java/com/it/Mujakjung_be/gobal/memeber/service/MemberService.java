package com.it.Mujakjung_be.gobal.memeber.service;

import com.it.Mujakjung_be.gobal.memeber.dto.JoinRequest;
import com.it.Mujakjung_be.gobal.memeber.entity.MemberEntity;
import com.it.Mujakjung_be.gobal.memeber.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor // final 필드 생성자 자동 생성 , 의존성 주입
public class MemberService {

    private final MemberRepository repository;
    private final PasswordEncoder encoder;


    public void save(JoinRequest request){
        // 이메일 중복
        repository.findByEmail(request.getEmail()).ifPresent(m-> {throw new IllegalArgumentException("이미 가입된 이메일입니다."); });
        MemberEntity member = new MemberEntity();
        member.setEmail(request.getEmail());
        member.setPassword(encoder.encode(request.getPassword()));
        member.setName(request.getName());

        repository.save(member);
    }




}
