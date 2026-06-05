package com.it.Mujakjung_be.global.member.repository;

import com.it.Mujakjung_be.global.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    // 이메일 존재 여부 확인
    boolean existsByEmail(String email);

    // 이메일로 회원 조회
    Optional<MemberEntity> findByEmail(String email);
}