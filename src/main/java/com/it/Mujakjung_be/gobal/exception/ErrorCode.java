
package com.it.Mujakjung_be.gobal.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

// 에러 코드들을 관리하는 enum 클래스
// 애플리케이션에서 발생하는 에러를 한 곳에서 정의하기 위해 사용
public enum ErrorCode {

    // BAD_REQUEST 에러 정의
    // HttpStatus.BAD_REQUEST → HTTP 상태 코드 400
    // "잘 못된 요청입니다" → 클라이언트에게 보여줄 에러 메시지
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘 못된 요청입니다");

    // HTTP 상태 코드 (예: 400, 404, 403 등)
    private final HttpStatus status;

    // 에러 메시지
    private final String message;

    // enum 생성자
    // BAD_REQUEST(...) 처럼 enum이 생성될 때 값이 여기로 전달됨
    ErrorCode(HttpStatus status ,String message){
        this.status = status;
        this.message = message;
    }

    // HTTP 상태 코드 반환
    public HttpStatus getStatus (){
        return status;
    }

    // 에러 메시지 반환
    public String getMessage(){
        return message;
    }
}