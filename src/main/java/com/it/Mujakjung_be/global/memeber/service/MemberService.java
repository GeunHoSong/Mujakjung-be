package com.it.Mujakjung_be.global.memeber.service;

import com.it.Mujakjung_be.global.memeber.dto.*;
import com.it.Mujakjung_be.global.memeber.entity.MemberEntity;
import com.it.Mujakjung_be.global.memeber.entity.Role;
import com.it.Mujakjung_be.global.memeber.repository.MemberRepository;
import com.it.Mujakjung_be.global.memeber.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor // final이 붙은 필드들을 파라미터로 받는 생성자를 자동으로 생성 (의존성 주입)
public class MemberService {

    private final MemberRepository repository; // DB 접근을 위한 레포지토리
    private final PasswordEncoder encoder;    // 비밀번호 암호화 도구
    private final JwtUtil jwtUtil;            // JWT 토큰 생성 및 검증 도구

    /**
     * 회원 가입 로직
     * 사용자가 입력한 정보를 DB에 저장하기 전에 검증하고 암호화함
     */
    public void save(JoinRequest request, MultipartFile profileImage){
        // 1. 이메일 중복 체크 (DB에 이미 해당 이메일이 있는지 확인)
        if (repository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("이미 가입된 이메일 입니다");
        }

        // 2. 새로운 회원 엔티티 생성 및 데이터 세팅
        MemberEntity member = new MemberEntity();
        member.setEmail(request.getEmail());
        // 중요: 비밀번호는 반드시 암호화(encode)해서 저장해야 함!
        member.setPassword(encoder.encode(request.getPassword()));
        member.setName(request.getName());
        member.setPhone(request.getPhone());
        member.setGender(request.getGender());
        member.setRole(Role.USER); // 가입 시 기본 권한은 USER로 설정
        if (profileImage != null && !profileImage.isEmpty() ){
            //  실제 저장 시에는 파일명이 겹치지 않게 UUID 같은 걸 붙이는 게 좋아!
            String originFileName  = profileImage.getOriginalFilename();
            // 이 부분이 성별 세팅하듯이 프로필을 만드는 부분이야!
            member.setProfileTmg(originFileName);
        }

        // 3. DB에 최종 저장
        repository.save(member);
    }

    /**
     * 로그인 로직
     * 이메일과 비밀번호를 확인하고, 맞으면 JWT 토큰을 발급함
     */
    public LoginResponse login(LoginRequest request){
        // 1. 입력받은 이메일로 회원 정보 찾기
        MemberEntity en = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("이메일 또는 비밀 번호가 없습니다"));

        // 2. 비밀번호 검증 (암호화된 비번과 사용자가 입력한 비번이 맞는지 확인)
        if (!encoder.matches(request.getPassword(), en.getPassword())){
            throw new IllegalArgumentException("이메일 또는 비밀 번호가 틀렸습니다");
        }

        // 3. 로그인 성공 시 JWT 토큰 생성 후 반환
        String token = jwtUtil.createToken(en.getEmail());
        return new LoginResponse(token);
    }

    /**
     * 마이페이지 정보 조회
     * 특정 이메일 사용자의 정보를 가져와 DTO로 변환하여 반환
     */
    public MyPageResponse getMyPage(String email) {
        // 1. 이메일로 사용자 정보 조회
        MemberEntity e = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다"));

        // 2. 엔티티(DB 데이터)를 응답용 DTO(MyPageResponse)로 옮겨 담기
        MyPageResponse response = new MyPageResponse();
        response.setEmail(e.getEmail());
        response.setName(e.getName());
        response.setPhone(e.getPhone());
        response.setGender(e.getGender());
        response.setAddress(e.getAddress());
        response.setRole(e.getRole().name()); // Enum 타입을 문자열로 변환

        return response;
    }

    /**
     * 프로필 업데이트 (추가 정보 입력)
     * 닉네임, 한줄소개, 프로필 이미지를 수정함
     * @Transactional: DB 작업 중 하나라도 실패하면 다시 되돌림(Rollback)
     */
    @Transactional
    public void updateProfile(String email, ProfileRequest request){
        // 1. 수정할 사용자 정보 가져오기
        MemberEntity member = repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        // 2. 요청 데이터가 있을 때만 해당 필드를 업데이트 (Null 체크)
        if (request.getNickname() != null) member.setNickname(request.getNickname());
        if (request.getBio() != null) member.setBio(request.getBio());
        if (request.getProfileTmgUrl() != null) member.setProfileTmg(request.getProfileTmgUrl());

        // @Transactional이 걸려있어서 명시적으로 save를 안 해도 변경사항이 DB에 반영됨 (더티 체킹)
    }

    public boolean login(String email , String password){
        Optional<MemberEntity> member  = repository.findByEmail(email);
        // 사용자 가 존재 하고 비밀 번호 일치 하면 확인
        if(member.isPresent()){
            return member.get().getPassword().equals(password);
        }
        return false;
    }
}