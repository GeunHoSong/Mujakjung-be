package com.it.Mujakjung_be.gobal.exception;

import lombok.Getter;

// Lombok 어노테이션
// 모든 필드의 getter 메서드를 자동 생성해줌
@Getter
public class CustomException extends RuntimeException {

    // ErrorCode enum을 저장하는 필드
    // 어떤 에러인지 (400, 404 등)와 메시지를 함께 관리하기 위해 사용
    private final ErrorCode errorCode;

    // 생성자
    // 예외를 발생시킬 때 ErrorCode를 전달받아서 저장함
    public CustomException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }
}