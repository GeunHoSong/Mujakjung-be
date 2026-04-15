package com.it.Mujakjung_be.global.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class JoinRequest {
    // 이메일 형식 검증
    @Email(message = "올바른 이메일 형식이 아닙니다")
    // 반값 방지
    @NotBlank(message = "이메일을 필수 입니다")
    private String email;
    // 비밀 번호 방지
    @NotBlank(message = "비밀 번호 필수 입니다")
    private String password;
    @NotBlank(message = "2차 비밀 번호를 입력을 하세요")
    private String ConfirmPassword;
    @NotBlank
    private String name;
    private String phone;
    private String gender;
    // 프로필 관련 추가 정보
    private String nickname; // 서비스에서 사용할 방법
    private String profileTmgUrl; // 프로필 이미지 경로 (기본값 설정 가능)
    private String bio; // 한줄 소개
    // 카카오 주소  api 정보
    private String zipcode;
    private String city;
    private String street;

}
