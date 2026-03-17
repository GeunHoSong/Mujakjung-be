package com.it.Mujakjung_be.gobal.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘 못된 요청입니다");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status ,String message){
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus (){
        return status;
    }

    public String getMessage(){
        return message;
    }
}
