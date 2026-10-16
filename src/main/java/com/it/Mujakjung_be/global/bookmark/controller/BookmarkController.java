package com.it.Mujakjung_be.global.bookmark.controller;

import com.it.Mujakjung_be.global.bookmark.entity.BookmarkEntity;
import com.it.Mujakjung_be.global.bookmark.repository.BookmarkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookmarks")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkRepository repository;

    // 내가 찜한 여행/맛집 목록 조회
    @GetMapping
    public ResponseEntity<List<BookmarkEntity>> getBookmarks(){
        // TODO: 나중에 현재 로그인한 유저의 ID를 동적으로 가져오도록 수정해야 합니다.
        // 일단 테스트로 memberId가 1인 데이터들을 가져오도록 해볼게요.
        Long loginMemberId = 1L;

        List<BookmarkEntity> bookmarkEntities = repository.findByMemberId(loginMemberId);
        return ResponseEntity.ok(bookmarkEntities);
    }


}
