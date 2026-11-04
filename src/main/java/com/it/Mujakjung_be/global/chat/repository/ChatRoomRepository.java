package com.it.Mujakjung_be.global.chat.repository;

import com.it.Mujakjung_be.global.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository  extends JpaRepository<ChatRoom , Long> {
    List<ChatRoom> findByStatus(String status);
    @Modifying
    @Query("""
        UPDATE ChatRoom c
        SET c.agentEmail = :agentEmail,
            c.status = 'CHATTING'
        WHERE c.roomId = :roomId
          AND c.status = 'WAITING'
          AND c.agentEmail IS NULL
        """)
    int acceptRoom(
            @Param("roomId") Long roomId,
            @Param("agentEmail") String agentEmail
    );
    List<ChatRoom> findByAgentEmailAndStatus(
            String agentEmail,
            String status
    );
}
