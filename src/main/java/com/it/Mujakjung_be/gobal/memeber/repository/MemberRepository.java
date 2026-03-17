package com.it.Mujakjung_be.gobal.memeber.repository;

import com.it.Mujakjung_be.gobal.memeber.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

    boolean existsByEmail (String email);


    Optional<MemberEntity> findByEmail(String email);
}
