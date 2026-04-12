package com.it.Mujakjung_be.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// 전역 예외 처리 클래스
// 컨트롤러에서 발생하는 예외를 여기서 한 번에 처리함
@RestControllerAdvice
public class GlobalExceptionHandler{

    // CustomException이 발생했을 때 실행되는 메서드
    // @ExceptionHandler는 특정 예외를 잡아서 처리할 수 있게 해줌
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<String> handleCustomException(CustomException e){

        // ResponseEntity.status(...)
        // → HTTP 상태코드를 설정 (예: 400, 404, 403 등)

        // e.getErrorCode().getStatus()
        // → ErrorCode enum에 저장된 HttpStatus 값을 가져옴

        // body(...)
        // → 클라이언트에게 전달할 메시지

        return ResponseEntity
                .status(e.getErrorCode().getStatus()) // HTTP 상태 코드 설정
                .body(e.getErrorCode().getMessage()); // 에러 메시지 반환
    }
}