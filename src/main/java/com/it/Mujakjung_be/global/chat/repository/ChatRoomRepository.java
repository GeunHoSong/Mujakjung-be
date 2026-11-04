package com.it.Mujakjung_be.global.chat.repository;

import com.it.Mujakjung_be.global.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository  extends JpaRepository<ChatRoom , Long> {
}
