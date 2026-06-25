package com.it.Mujakjung_be.global.comment.entity;

import com.it.Mujakjung_be.global.board.entity.BoardEntity;
import com.it.Mujakjung_be.global.member.entity.MemberEntity;
import com.it.Mujakjung_be.global.notice.entity.Notice;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")// 어떤 공지사항의 댓글인지
    private BoardEntity  board;

    // 누가 작성을 했는지 (로그인 유지)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private MemberEntity member;

    private LocalDateTime regDate;


}
