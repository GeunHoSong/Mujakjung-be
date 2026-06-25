package com.it.Mujakjung_be.global.comment.repository;

import com.it.Mujakjung_be.global.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByBoardIdOrderByRegDateDesc(Long boardId);
}
