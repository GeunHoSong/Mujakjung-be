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
        return repository.findByMemberId_Id(memberId)
                .map(entity -> {
                    MyPageDto dto = new MyPageDto();
                    dto.setEmail(entity.getEmail());
                    dto.setNickname(entity.getNickname());
                    dto.setBio(entity.getBio());
                    return dto;
                })
                .orElseGet(() -> {
                    // 마이페이지가 없는 회원을 위해 기본 DTO 반환
                    MyPageDto emptyDto = new MyPageDto();
                    emptyDto.setNickname("닉네임을 설정해주세요");
                    return emptyDto;
                });
    }

    public void updateMypage(Long id , MyPageDto  dto) {
        MyPageEntity  m = repository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 마이페이페이지 없습니다"));

        // 엔티티에 만들어둔 update 매서드 호출
        m.update(dto.getNickname(), dto.getBio());
        // / @Transactional 때문에 별도의 save() 없이도 자동으로 DB에 반영돼!
    }
}
