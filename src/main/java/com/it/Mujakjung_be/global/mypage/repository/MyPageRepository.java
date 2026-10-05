package com.it.Mujakjung_be.global.mypage.repository;

import com.it.Mujakjung_be.global.mypage.entity.MyPageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// MyPageRepository.java
public interface MyPageRepository extends JpaRepository<MyPageEntity, Long> {

    // 1. 이미 정의된 findById를 굳이 다시 선언할 필요는 없지만,
    //    만약 커스텀하게 만들고 싶다면 반드시 타입을 적어야 합니다.
    Optional<MyPageEntity> findById(Long id);

    // 2. 만약 memberId로 찾고 싶다면 이렇게 하세요.
    Optional<MyPageEntity> findByMemberId(Long memberId);
}

