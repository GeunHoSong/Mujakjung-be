package com.it.Mujakjung_be.global.chat.service;

import com.it.Mujakjung_be.global.chat.entity.ChatRoom;
import com.it.Mujakjung_be.global.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    public List<ChatRoom> findWaitingRooms(){
        return repository.findByStatus("WAITING");
    }

    // ④ 상담사가 상담 요청 수락
    @Transactional
    public ChatRoom acceptRoom(Long roomId, String agentEmail){
        int updated = repository.acceptRoom(roomId, agentEmail);
        if(updated == 0){
            throw  new IllegalArgumentException("이미 수락을 되었거나 존재 하지 않는 상담방입니다");
        }
        return  repository.findById(roomId).orElseThrow(()-> new IllegalArgumentException("상담방을 찾을 수 없습니다"));
    }
}

