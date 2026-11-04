package com.it.Mujakjung_be.global.chat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "chat_room") // MySQL 테이블 이름 지정
public class ChatRoom {
    // 상담방 고유 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    // 상담을 요청한 회원 이메일
    @Column(nullable = false)
    private String memberEmail;

    // 담당 상담사 이메일
    private String agentEmail;

    // 상담 상태
    private String  status;

}
