package com.it.Mujakjung_be.global.notice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String writer;
    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    private LocalDateTime regDate;

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
