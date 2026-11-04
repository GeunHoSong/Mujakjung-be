package com.it.Mujakjung_be.global.agent.controller;

import com.it.Mujakjung_be.global.chat.entity.ChatRoom;
import com.it.Mujakjung_be.global.chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/agent/rooms")
public class AgentChatController {
    private final ChatRoomService service;

    @GetMapping("/waiting")
    public ResponseEntity<List<ChatRoom>> getWaitingRoom(){
        List<ChatRoom> rooms = service.findWaitingRooms();
        return ResponseEntity.ok(rooms);
    }

    @PatchMapping("/{roomId}/accept")
    public ResponseEntity<ChatRoom> acceptRoom(@PathVariable Long roomId, Authentication au){
        String agentEmail = au.getName();
        ChatRoom room = service.acceptRoom(roomId, agentEmail);
        return ResponseEntity.ok(room);
    }
}
