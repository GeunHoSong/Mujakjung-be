package com.it.Mujakjung_be.global.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class JoinRequest {
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @NotBlank(message = "이메일은 필수입니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,16}$",
            message = "비밀번호는 8~16자의 영문, 숫자, 특수문자를 각각 최소 1개 이상 포함해야 합니다."
    )
    private String password;

    @NotBlank(message = "2차 비밀번호를 입력하세요")
    private String confirmPassword; // 소문자 c로 통일

    @NotBlank(message = "이름은 필수입니다")
    private String name;

    private String phone;
    private String gender;

    private String nickname;
    private String profileImgUrl; // 오타 수정 완료
    private String bio;

    // 프론트엔드 상태값과 일치시킴
    private String zipcode;
    private String address;
    private String detailAddress;
}