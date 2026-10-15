package com.it.Mujakjung_be.global.mypage.dto;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageResponse {
    private String email;
    private String name;
    private String phone;
    private String gender;
    private String address;
    private String role;
}
