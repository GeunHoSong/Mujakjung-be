package com.it.Mujakjung_be.global.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "board")
@AllArgsConstructor
@NoArgsConstructor
public class BoardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String content;
    private String writer;

    private LocalDateTime regDate;

    private String fileName;
    private String filePath;



    @Builder
    public BoardEntity(Long id, String title, String content, String writer ,String fileName, String filePath) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.regDate = LocalDateTime.now();
        this.fileName = fileName;
        this.filePath = filePath;
    }


    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
