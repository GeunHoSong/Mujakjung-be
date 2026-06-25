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

    // 댓글 조회
    public List<CommentEntity> getComment(Long boardId){
        return repository.findByBoardIdOrderByRegDateDesc(boardId).stream().map(BoardDto::new).toList();

    }
}
