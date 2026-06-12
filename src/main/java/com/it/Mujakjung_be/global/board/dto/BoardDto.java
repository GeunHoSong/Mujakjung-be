package com.it.Mujakjung_be.global.board.dto;

import com.it.Mujakjung_be.global.board.entity.BoardEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {
    private Long id;
    private String title;
    private String writer;
    private String content;
    private String regDate;

    // 1. 엔티티를 DTO로 변환할 때 쓰는 생성자
    public BoardDto(BoardEntity entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.writer = entity.getWriter();
        this.regDate = entity.getRegDate() != null ? entity.getRegDate().toString() : "";
    }
} // 💡 중괄호를 여기까지만 유지해야 해!