package com.it.Mujakjung_be.global.comment.dto;

import com.it.Mujakjung_be.global.comment.entity.CommentEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {

    private Long id;
    private String content;
    private LocalDateTime regDate;
    private String writer;

    public CommentResponseDto(CommentEntity comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.regDate = comment.getRegDate();
        this.writer = comment.getMember().getNickname(); // 필요한 정보만 추출
    }

}
