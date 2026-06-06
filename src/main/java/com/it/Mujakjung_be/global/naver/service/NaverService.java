package com.it.Mujakjung_be.global.naver.service;

import com.it.Mujakjung_be.global.member.entity.MemberEntity;
import com.it.Mujakjung_be.global.member.entity.Role;
import com.it.Mujakjung_be.global.member.repository.MemberRepository;
import com.it.Mujakjung_be.global.member.util.JwtUtil;
import com.it.Mujakjung_be.global.naver.dto.NaverDto;
import com.it.Mujakjung_be.global.naver.util.NaverUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NaverService {

    private final MemberRepository repository;
    private final JwtUtil util;
    private final NaverUtil naverUtil;

    @Transactional
    public String processNaverLogin(String code, String state) {
        // 1. [HTTP 통신] 네이버 토큰 API 호출
        String naverAccessToken = naverUtil.getAccessToken(code, state);

        // 2. [HTTP 통신] Access Token으로 네이버 프로필 정보 가져오기
        NaverDto naverUser = naverUtil.getNaverUserInfo(naverAccessToken);

        // 💡 껍데기(Response) 안에서 실제 유저 데이터 객체 안전하게 꺼내기
        NaverDto.Response naverProfile = naverUser.getResponse();

        // 만약의 상황을 대비해 profile 자체가 null이거나 이메일이 null인지 검증
        if (naverProfile == null || naverProfile.getEmail() == null) {
            throw new IllegalArgumentException("네이버로부터 사용자 정보를 불러오지 못했습니다.");
        }

        // 3. DB에서 네이버 이메일로 가입된 기존 회원이 있는지 확인
        Optional<MemberEntity> member = repository.findByEmail(naverProfile.getEmail());
        MemberEntity member1;

        if (member.isPresent()) {
            // 3-1. 기존 회원이면 DB에서 가져오기
            member1 = member.get();
        } else {
            // 3-2. 신규 회원이면 네이버에서 받아온 정보로 자동 회원가입 진행!
            member1 = new MemberEntity();
            member1.setEmail(naverProfile.getEmail());
            member1.setName(naverProfile.getName());
            member1.setRole(Role.USER);
            member1.setPassword("NAVER_SOCIAL_LOGIN");
            repository.save(member1);
        }

        // 💡 주입받은 util을 사용해서 최종 JWT 토큰을 만들어 리턴해준다!!
        return util.createToken(member1.getEmail());
    }
}