
package com.it.Mujakjung_be.gobal.memeber.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

import java.lang.reflect.Member;

/**
 * 회원 정보를 저장하는 JPA 엔티티 클래스
 * → DB의 member 테이블과 매핑됨
 */
@Entity //  이 클래스가 JPA 엔티티임을 선언 (테이블과 매핑)
@Table(name = "member") // DB 테이블 이름 지정
@Setter
@Getter
@NoArgsConstructor
public class MemberEntity {

    @Id // 기본키(PK) 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // MySQL auto_increment 사용
    private Long Id;

    @Column(nullable = false, length = 50)
    //  NOT NULL + VARCHAR(50)
    private String email;

    @Column(nullable = false, length = 100)
    //  NOT NULL + VARCHAR(100)
    private String password;

    @Column(nullable = false , length = 30)
    //  NOT NULL + VARCHAR(30)
    private String name;

    private String phone;

    private String gender;
    private String nickname;
    private String profileTmg;
    private String bio; //자기 소개

    @Column(length = 100)
    private Address address; // 주소
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    public MemberEntity (String email, String password, String name, String phone, String gender, Address address, Role role  ,String nickname, String profileTmg, String bio ){
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.gender= gender;
        this.address = address;
        this.role = role;
        this.nickname = nickname;
        this.profileTmg=  profileTmg;
        this.bio = bio;
    }



    /**
     * JPA 기본 생성자 (필수)
     * - JPA가 리플렉션으로 객체 생성할 때 사용
     * - protected 또는 public 이어야 함
     */

}