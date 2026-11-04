
package com.it.Mujakjung_be.global.config;

import com.it.Mujakjung_be.global.member.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@RequiredArgsConstructor
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtUtil util;
    private final UserDetailsService service;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor ac =
                MessageHeaderAccessor.getAccessor(
                        message,
                        StompHeaderAccessor.class
                );

        if (ac == null) {
            return message;
        }

        // STOMP 연결 요청인지 확인
        if (StompCommand.CONNECT.equals(ac.getCommand())) {

            // Authorization 헤더 가져오기
            String auth = ac.getFirstNativeHeader("Authorization");

            if (auth == null || !auth.startsWith("Bearer ")) {
                throw new IllegalArgumentException("WebSocket 인증 토큰이 없습니다.");
            }

            // Bearer 부분 제거
            String token = auth.substring(7);

            // JWT 유효성 검사
            if (!util.validateToken(token)) {
                throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
            }

            // JWT에서 이메일 추출
            String email = util.getEmail(token);

            // 회원 정보 조회
            UserDetails details = service.loadUserByUsername(email);

            // 인증 객체 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            details,
                            null,
                            details.getAuthorities()
                    );

            // WebSocket 사용자 인증 정보 등록
            ac.setUser(authenticationToken);
        }

        return message;
    }
}
