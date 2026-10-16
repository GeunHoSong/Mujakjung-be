package com.it.Mujakjung_be.global.bookmark.repository;

import com.it.Mujakjung_be.global.bookmark.entity.BookmarkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity , Long> {
    List<BookmarkEntity> findByMemberId(Long memberId);
}
