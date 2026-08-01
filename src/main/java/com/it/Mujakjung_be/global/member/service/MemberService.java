package com.it.Mujakjung_be.global.member.service;

import com.it.Mujakjung_be.global.member.dto.*;
import com.it.Mujakjung_be.global.member.entity.MemberEntity;
import com.it.Mujakjung_be.global.member.entity.Role;
import com.it.Mujakjung_be.global.member.repository.MemberRepository;
import com.it.Mujakjung_be.global.member.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository repository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public void save(JoinRequest request, MultipartFile profileImage){
        // repository 오타 수정
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
        if (profileImage != null && !profileImage.isEmpty() ){
            String originFileName  = profileImage.getOriginalFilename();
            member.setProfileTmg(originFileName);
        }

        // repository 오타 수정
        repository.save(member);
    }

    public LoginResponse login(LoginRequest request){
        // repository 오타 수정
        MemberEntity en = repository.findByEmail(request.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("이메일 또는 비밀 번호를 찾을 수 없습니다"));

        if(!encoder.matches(request.getPassword(), en.getPassword())){
            throw new IllegalArgumentException("이메일 또는 비밀 번호를 찾을 수 없습니다");
        }

        String token = jwtUtil.createToken(en.getEmail());
        return new LoginResponse(token, en.getRole().name(), en.getName());
    }

    public MyPageResponse getMyPage(String email) {
        // repository 오타 수정
        MemberEntity e = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다"));

        MyPageResponse response = new MyPageResponse();
        response.setEmail(e.getEmail());
        response.setName(e.getName());
        response.setPhone(e.getPhone());
        response.setGender(e.getGender());
        response.setAddress(e.getAddress());
        response.setRole(e.getRole().name());

        return response;
    }

    @Transactional
    public void updateProfile(String email, ProfileRequest request){
        // repository 오타 수정
        MemberEntity member = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        if (request.getNickname() != null) member.setNickname(request.getNickname());
        if (request.getBio() != null) member.setBio(request.getBio());
        if (request.getProfileTmgUrl() != null) member.setProfileTmg(request.getProfileTmgUrl());
    }


    public List<MemberResponse> findAllMembersForAdmin() {
        // repository 오타 수정
        List<MemberEntity> all = repository.findAll();

        // 람다식 괄호 추가 또는 타입 생략 (m -> 로 변경)
        return all.stream().map(m -> new MemberResponse(
                m.getId(),
                m.getName(),
                m.getEmail(),
                m.getRole().name(),
                ""
        )).toList();
    }

    public List<MemberResponse> finllAll(){
        List<MemberEntity> members  = repository.findAll();

        return members.stream().map(MemberResponse::new).collect(Collectors.toList());
    }
}