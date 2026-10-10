package com.it.Mujakjung_be.global.mypage.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.it.Mujakjung_be.global.member.entity.MemberEntity;

@Entity
@Getter
@Table(name = "mypage")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MyPageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String nickname;
    private String bio;

    @Column(name = "profile_img") // 프로필 이미지 파일명 필드 추가
    private String profileImg;

    @OneToOne
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    // 닉네임, 자기소개, 프로필 이미지를 한 번에 업데이트하는 메서드
    public void update(String nickname, String bio, String profileImg){
        this.nickname = nickname;
        this.bio = bio;
        if (profileImg != null) {
            this.profileImg = profileImg;
        }
    }

}
