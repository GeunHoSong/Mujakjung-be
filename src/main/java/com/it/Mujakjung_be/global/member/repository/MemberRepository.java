package com.it.Mujakjung_be.global.member.repository;

import com.it.Mujakjung_be.global.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    boolean existsByEmail (String email);


    Optional<MemberEntity> findByEmail(String email);
}
