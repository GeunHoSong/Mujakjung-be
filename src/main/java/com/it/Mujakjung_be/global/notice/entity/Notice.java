package com.it.Mujakjung_be.global.notice.entity;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Getter // Lombok 사용 시
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String writer;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false, updatable = false)
    private LocalDateTime regDate;

    // 데이터가 저장되기 직전에 자동으로 날짜 설정
    @PrePersist
    protected void onCreate() {
        regDate = LocalDateTime.now();
    }
}