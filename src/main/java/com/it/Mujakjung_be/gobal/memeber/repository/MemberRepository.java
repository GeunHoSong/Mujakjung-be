package com.it.Mujakjung_be.gobal.memeber.repository;

import com.it.Mujakjung_be.gobal.memeber.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
}
