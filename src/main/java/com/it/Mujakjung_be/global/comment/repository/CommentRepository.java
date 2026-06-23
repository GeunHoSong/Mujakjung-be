package com.it.Mujakjung_be.global.comment.repository;

import com.it.Mujakjung_be.global.comment.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity , Long> {
}
