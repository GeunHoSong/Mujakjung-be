package com.it.Mujakjung_be.global.board.repository;

import com.it.Mujakjung_be.global.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
}
