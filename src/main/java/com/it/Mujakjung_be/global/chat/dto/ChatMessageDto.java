package com.it.Mujakjung_be.global.chat.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDto {

    // 상담방 식별 번호
    private String roomId;
    // 메시지 발신자 식별값
    private String sender;

    // 메시지 내용
    private String content;

    // 메시지 종료
    private String type;
}
