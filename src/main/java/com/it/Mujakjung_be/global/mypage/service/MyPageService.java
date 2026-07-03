package com.it.Mujakjung_be.global.mypage.service;

import com.it.Mujakjung_be.global.member.repository.MemberRepository;
import com.it.Mujakjung_be.global.mypage.dto.MyPageDto;
import com.it.Mujakjung_be.global.mypage.entity.MyPageEntity;
import com.it.Mujakjung_be.global.mypage.repository.MyPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MyPageRepository repository;
    private final MemberRepository memberRepository;

    public MyPageDto getMypage(Long memberId){
        MyPageEntity entity = repository.findById(memberId).orElseThrow(()-> new IllegalArgumentException("해당 마이 패이지 가 없습니다"));
        MyPageDto  dto = new MyPageDto();

        dto.setEmail(entity.getEmail());
        dto.setNickname(entity.getNickname());
        dto.setBio(entity.getBio());
        return dto;

    }
}
