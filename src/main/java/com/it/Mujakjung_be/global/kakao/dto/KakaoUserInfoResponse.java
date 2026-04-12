package com.it.Mujakjung_be.global.kakao.dto;

import lombok.Data;

@Data
public class KakaoUserInfoResponse {

    // 유저 정보 응답형
    private Long id;
    private KakaoTokenResponse kakao_account;



}
