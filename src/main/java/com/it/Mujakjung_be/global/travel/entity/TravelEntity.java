package com.it.Mujakjung_be.global.travel.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class TravelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category; //  국내 해외
    private String title;  // 이름
    private String location;// 위치

    @Column(columnDefinition = "TEXT")
    private String content; // 상세 내용


    private String imageName; // 서버에 저장된 이미지 파일명 (예: uuid_image.png)
    private String imagePath; // 이미지 접근 경로

    @Column(updatable = false)
    private LocalDateTime regDate; // 등록 날짜


    @PrePersist
    public void prePersist(){
        this.regDate = LocalDateTime.now();
    }
}
