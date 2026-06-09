package com.it.Mujakjung_be.global.notice.repository;

import com.it.Mujakjung_be.global.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice , Long> {
}
