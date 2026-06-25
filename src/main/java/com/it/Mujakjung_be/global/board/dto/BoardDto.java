package com.it.Mujakjung_be.global.board.dto;

import com.it.Mujakjung_be.global.board.entity.BoardEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDto {
    private Long id;
    private String title;
    private String writer; // 👈 작성자 필드 확인
    private String content;
    private String regDate;
    private MultipartFile file;

    // Entity -> DTO 변환 시 작성자(writer)를 반드시 포함합니다.
    public static BoardDto fromEntity(BoardEntity entity) {
        return BoardDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .writer(entity.getWriter()) // 여기서 writer가 들어가고 있는지 디버깅 필요!
                .content(entity.getContent())
                .regDate(entity.getRegDate() != null ? entity.getRegDate().toString() : "")
                .build();
    }
}