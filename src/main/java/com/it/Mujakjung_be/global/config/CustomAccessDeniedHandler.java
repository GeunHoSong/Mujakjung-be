package com.it.Mujakjung_be.global.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component; // 💡 빈 등록을 위해 필수!
import java.io.IOException;

/**
 * [403 Forbidden 처리기]
 * 로그인은 성공해서 누구인지는 알지만(Authentication 완료),
 * 요청한 리소스에 접근할 권한(Authority)이 없을 때 동작을 정의해.
 */
@Component // 스프リング이 이 클래스를 관리하도록 빈(Bean)으로 등록
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    /**
     * handle 메서드는 권한 부족 예외 발생 시 자동으로 호출돼.
     * @param request : 요청 정보
     * @param response : 응답 정보
     * @param accessDeniedException : 권한 거부 예외 객체
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {

        // 1. 응답 상태 코드를 403(Forbidden)으로 설정
        // 사용자가 누구인지는 알지만, "너 여기 들어올 권한 없어!"라고 알려주는 거야.
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        // 2. 응답 데이터 형식을 JSON으로 설정
        response.setContentType("application/json;charset=UTF-8");

        // 3. 클라이언트(프론트엔드)가 에러 내용을 알 수 있도록 JSON 메시지 작성
        // 이 메시지를 보고 프론트에서 "관리자만 접근 가능한 페이지입니다" 같은 경고창을 띄워줄 수 있어.
        response.getWriter().write("{\"message\": \"권한이 필요합니다\", \"status\": 403}");

        // 💡 팁: 어떤 권한이 부족해서 막혔는지 로그를 남겨두면 나중에 운영할 때 편해!
    }
}