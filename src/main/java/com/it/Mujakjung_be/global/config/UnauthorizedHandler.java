package com.it.Mujakjung_be.global.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component; // 💡 꼭 추가되어야 함!
import java.io.IOException;

/**
 * [401 Unauthorized 처리기]
 * AuthenticationEntryPoint 인터페이스를 구현하여,
 * '인증(Authentication)'되지 않은 사용자가 보호된 API에 접근했을 때 동작을 정의해.
 */
@Component // 스프링이 이 클래스를 관리하도록 빈(Bean)으로 등록함
public class UnauthorizedHandler implements AuthenticationEntryPoint {

    /**
     * commence 메서드는 인증 실패 시 자동으로 실행되는 메서드야.
     * @param request : 클라이언트의 요청 정보
     * @param response : 서버가 클라이언트에게 보낼 응답 정보
     * @param authException : 발생한 인증 예외 (왜 실패했는지 정보가 담겨있어)
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // 1. 응답 상태 코드를 401(Unauthorized)로 설정함
        // SC_UNAUTHORIZED는 숫자 401을 의미하는 상수야.
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 2. 서버가 보내는 데이터의 형식이 'JSON'이고 'UTF-8' 인코딩임을 프론트엔드에 알려줌
        // ⚠️ 주의: 콜론(:) 대신 세미콜론(;)을 쓰는 게 정석이야 (application/json;charset=UTF-8)
        response.setContentType("application/json;charset=UTF-8");

        // 3. 실제 응답 바디(Body)에 JSON 형태의 에러 메시지를 작성함
        // 프론트엔드(React 등)에서는 이 메시지를 받아서 "로그인이 필요합니다"라고 띄워줄 수 있어.
        response.getWriter().write("{\"message\":\"인증이 필요합니다\", \"status\": 401}");

        // 💡 팁: 나중에 로그를 남기고 싶다면 여기서 log.error(authException.getMessage())를 쓸 수 있어!
    }
}