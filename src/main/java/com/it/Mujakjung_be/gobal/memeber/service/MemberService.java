package com.it.Mujakjung_be.gobal.memeber.service;

import com.it.Mujakjung_be.gobal.memeber.dto.JoinRequest;
import com.it.Mujakjung_be.gobal.memeber.dto.LoginRequest;
import com.it.Mujakjung_be.gobal.memeber.dto.LoginResponse;
import com.it.Mujakjung_be.gobal.memeber.dto.MyPageResponse;
import com.it.Mujakjung_be.gobal.memeber.entity.MemberEntity;
import com.it.Mujakjung_be.gobal.memeber.entity.Role;
import com.it.Mujakjung_be.gobal.memeber.repository.MemberRepository;
import com.it.Mujakjung_be.gobal.memeber.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor // final 필드 생성자 자동 생성 , 의존성 주입
public class MemberService {

    private final MemberRepository repository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    // 회원 가입
    public void save(JoinRequest request){
        // 이메일 중복
        if (repository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 가입된 이메일 입니다");
        }

        MemberEntity member = new MemberEntity();
        member.setEmail(request.getEmail());
        member.setPassword(encoder.encode(request.getPassword()));
        member.setName(request.getName());
        member.setPhone(request.getPhone());
        member.setGender(request.getGender());
        member.setRole(Role.USER);


        repository.save(member);
    }

    //로그인
    public LoginResponse login(LoginRequest request){
        // 이메일 있는지 없느지 검증
        MemberEntity en = repository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀 번호가 없습니다 "));
        // 로그인 비밀 번호 검증
        if (!encoder.matches(request.getPassword(), en.getPassword())){
            throw new IllegalArgumentException("이메일 또는 비밀 번호가 틀렸습니다");
        }
        // JWT 생성
        String token = jwtUtil.createToken(en.getEmail());
        return new LoginResponse(token);
    }
    // 마이 페이지
    public MyPageResponse getMyPage(String email) {

        MemberEntity e = repository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("회원 정보를 찾을 수 없습니다"));
        MyPageResponse response = new MyPageResponse();
        response.setEmail(e.getEmail());
        response.setName(e.getName());
        response.setPhone(e.getPhone());
        response.setGender(e.getGender());
        response.setAddress(e.getAddress());
        response.setRole(e.getRole().name());
        return response;
    }

}
