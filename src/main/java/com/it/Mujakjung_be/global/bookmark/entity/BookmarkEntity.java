package com.it.Mujakjung_be.global.bookmark.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bookmark")
@Getter
@Setter
public class BookmarkEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long  memberId;

    @Column(nullable = false)
    private String placeName;


    private String category;

    private String address;
}
