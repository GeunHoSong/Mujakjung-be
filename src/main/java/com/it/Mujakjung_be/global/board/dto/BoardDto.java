package com.it.Mujakjung_be.global.board.dto;

import com.it.Mujakjung_be.global.board.entity.BoardEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private String regDate;
    // 1. 엔티티를 DTO로 변환할 때 쓰는 생성자
    public BoardDto(BoardEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.writer = entity.getWriter();
        this.regDate = entity.getRegDate() != null ? entity.getRegDate().toString() : "";
    }
}
