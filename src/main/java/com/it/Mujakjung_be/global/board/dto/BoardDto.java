package com.it.Mujakjung_be.global.board.dto;

import com.it.Mujakjung_be.global.board.entity.BoardEntity;
import com.it.Mujakjung_be.global.comment.entity.CommentEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
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
    private LocalDateTime regDate; // 타입을 LocalDateTime으로 유지
    private MultipartFile file;

    public BoardDto(CommentEntity commentEntity) {
        this.id = commentEntity.getId();
        this.content = commentEntity.getContent();
        this.regDate = commentEntity.getRegDate();
    }

    // ... (CommentEntity 생성자는 나중에 다른 DTO로 분리하는 것을 추천합니다)

    public static BoardDto fromEntity(BoardEntity entity) {
        return BoardDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .writer(entity.getWriter())
                .content(entity.getContent())
                .regDate(entity.getRegDate()) // .toString()을 제거하여 타입 일치
                .build();
    }
}