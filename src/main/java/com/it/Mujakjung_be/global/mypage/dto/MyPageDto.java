package com.it.Mujakjung_be.global.mypage.dto;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageDto {
    private Long id;
    private String email;
    private String nickname;
    private String bio;
    private String profileImg;
}
