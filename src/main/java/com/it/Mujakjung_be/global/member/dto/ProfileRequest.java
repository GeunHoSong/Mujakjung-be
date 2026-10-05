package com.it.Mujakjung_be.global.member.dto;

import lombok.Data;

@Data
public class ProfileRequest {
    private String nickname;
    private String bio;
    private String profileTmgUrl;
}
