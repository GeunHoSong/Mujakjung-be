package com.it.Mujakjung_be.global.chat.service;

import com.it.Mujakjung_be.global.chat.entity.ChatRoom;
import com.it.Mujakjung_be.global.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository repository;

    @Transactional
    public ChatRoom createRoom(String memberEmail){
        ChatRoom room = new ChatRoom();
        room.setMemberEmail(memberEmail);
        room.setStatus("WAITING");

        return repository.save(room);
    }

    @Transactional(readOnly = true)
    public ChatRoom findRoom(Long roomId){
        return repository.findById(roomId).orElseThrow(()-> new IllegalArgumentException("상담방을 찾을 수 없습니다"));
    }
}

