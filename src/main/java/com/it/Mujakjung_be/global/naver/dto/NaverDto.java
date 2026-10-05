package com.it.Mujakjung_be.global.naver.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NaverDto {
    private String resultcode;
    private String message;
    private Response response; // 👈 네이버 JSON의 "response" 객체를 받아줄 껍데기 필드!

    @Getter
    @Setter
    public static class Response { // 👈 static class 오타 없이 정확하게!
        private String id;
        private String email;
        private String name;
    }
}