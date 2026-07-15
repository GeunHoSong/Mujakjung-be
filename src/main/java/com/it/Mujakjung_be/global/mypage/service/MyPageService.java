package com.it.Mujakjung_be.global.mypage.service;

import com.it.Mujakjung_be.global.member.repository.MemberRepository;
import com.it.Mujakjung_be.global.mypage.dto.MyPageDto;
import com.it.Mujakjung_be.global.mypage.entity.MyPageEntity;
import com.it.Mujakjung_be.global.mypage.repository.MyPageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MyPageService {
    private final MyPageRepository repository;
    private final MemberRepository memberRepository;

    public MyPageDto getMypage(Long memberId) {
        return repository.findByMemberId(memberId)
                .map(entity -> {
                    MyPageDto dto = new MyPageDto();
                    dto.setId(entity.getId()); // <--- 이 코드를 반드시 추가해야 해!
                    dto.setEmail(entity.getEmail());
                    dto.setNickname(entity.getNickname());
                    dto.setBio(entity.getBio());
                    return dto;
                })
                .orElseGet(() -> {
                    MyPageDto emptyDto = new MyPageDto();
                    emptyDto.setNickname("닉네임을 설정해주세요");
                    return emptyDto;
                });
    }

    public void updateMypage(Long id, MyPageDto dto) {
        // 1. 로그인한 이메일로 엔티티를 찾음
        MyPageEntity m = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("마이페이지가 존재하지 않습니다."));

        // 2. 업데이트
        m.update(dto.getNickname(), dto.getBio());
    }
}

