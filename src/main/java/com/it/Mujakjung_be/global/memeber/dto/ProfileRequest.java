package com.it.Mujakjung_be.global.memeber.dto;

import lombok.Data;

@Data
public class ProfileRequest {
    private String nickname;
    private String bio;
    private String profileTmgUrl;
}
