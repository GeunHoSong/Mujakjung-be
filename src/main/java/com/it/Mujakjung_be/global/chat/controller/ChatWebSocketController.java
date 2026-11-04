package com.it.Mujakjung_be.global.chat.controller;

import com.it.Mujakjung_be.global.chat.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {
    private final SimpMessagingTemplate si;

    // react 에서 보낸 채팅 메시지를 받는 메서드
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageDto dto){
        // 상담방 구독자에게 메시지 전달
        si.convertAndSend("/topic/chat/" + dto.getRoomId(), dto);
    }
}
