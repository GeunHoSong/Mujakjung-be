package com.it.Mujakjung_be.global.chat.controller;

import com.it.Mujakjung_be.global.chat.entity.ChatRoom;
import com.it.Mujakjung_be.global.chat.service.ChatRoomService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat/room")
public class ChatRoomController {
    private final ChatRoomService service;

    @PostMapping
    public ResponseEntity<String> createRoom(Authentication auth){
        String email = auth.getName();
        ChatRoom room = service.createRoom(email);
       return  ResponseEntity.ok("상담방 생성");
    }

}
