package com.it.Mujakjung_be.global.notice.dto;

import com.it.Mujakjung_be.global.notice.entity.Notice;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeDto {
    // 💡 아래 필드들이 빠져 있어서 에러가 발생한 것입니다.
    private Long id;
    private String title;
    private String writer;
    private String content;
    private String regDate;

    // Entity를 받아 Dto로 변환하는 생성자
    public NoticeDto(Notice entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.writer = entity.getWriter();
        this.regDate = entity.getRegDate() != null ? entity.getRegDate().toString() : "";
    }
}