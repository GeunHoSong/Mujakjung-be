package com.it.Mujakjung_be.global.chat.controller;

import com.it.Mujakjung_be.global.chat.dto.ChatRequestDto;
import com.it.Mujakjung_be.global.chat.dto.ChatResponseDto;
import com.it.Mujakjung_be.global.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService service;

    @PostMapping
    public ResponseEntity<ChatResponseDto> chat(@RequestBody ChatRequestDto dto){

        String aiResponse = service.generateResponse(dto.getContent());

        return ResponseEntity.ok(new ChatResponseDto(aiResponse));
    }
}
