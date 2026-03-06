package com.it.Mujakjung_be.gobal.memeber.service;

import com.it.Mujakjung_be.gobal.memeber.dto.JoinRequest;
import com.it.Mujakjung_be.gobal.memeber.dto.LoginRequest;
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
        if (repository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 가입된 이메일 입니다");
        }

        MemberEntity member = new MemberEntity();
        member.setEmail(request.getEmail());
        member.setPassword(encoder.encode(request.getPassword()));
        member.setName(request.getName());

        repository.save(member);
    }

    //
    public void login(LoginRequest request){
        // 이메일 있는지 없느지 검증
        MemberEntity en = repository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("이메일이 없습니다"));
        // 로그인 비밀 번호 검증
        if (!encoder.matches(request.getPassword(), en.getPassword())){
            throw new IllegalArgumentException("비밀 번호 가 틀렸습니다");
        }

    }

}
