package com.it.Mujakjung_be.global.mypage.repository;

import com.it.Mujakjung_be.global.mypage.entity.MyPageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyPageRepository extends JpaRepository<MyPageEntity , Long> {
    Optional<MyPageEntity> findByMemberId_Id(Long memberId);
    Optional<MyPageEntity> findById(Long id);
}
