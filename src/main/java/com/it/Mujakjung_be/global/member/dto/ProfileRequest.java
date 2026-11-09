package com.it.Mujakjung_be.global.member.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileRequest {
    private String nickname;
    private String bio;
    private String profileTmgUrl;

    // 프로필 사진 업로드 파일
    private MultipartFile file;
}
