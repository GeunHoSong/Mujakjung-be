package com.it.Mujakjung_be.global.comment.service;

import com.it.Mujakjung_be.global.board.dto.BoardDto;
import com.it.Mujakjung_be.global.board.entity.BoardEntity;
import com.it.Mujakjung_be.global.comment.entity.CommentEntity;
import com.it.Mujakjung_be.global.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;

    // CommentService.java
    public List<CommentEntity> getComment(Long boardId) {
        return repository.findByBoardIdOrderByRegDateDesc(boardId); // map(BoardDto::new) 삭제
    }

    public void saveComment(CommentEntity comment) {
        repository.save(comment);
    }
}