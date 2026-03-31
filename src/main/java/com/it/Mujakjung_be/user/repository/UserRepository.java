package com.it.Mujakjung_be.user.repository;

import com.it.Mujakjung_be.user.entity.User;  // ✅ 이걸로 변경
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User , Long> {
    User findByKakaoId(Long kakaoId);
}