package com.it.Mujakjung_be.global.mypage.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Member;

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

    @OneToOne
    @JoinColumn(name = "member_id")

    private Member  member;

    public void update(String nickname, String bio){
        this.nickname = nickname;
        this.bio  = bio;
    }

}
