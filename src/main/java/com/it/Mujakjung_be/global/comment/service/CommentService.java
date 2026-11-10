package com.it.Mujakjung_be.global.comment.service;

import com.it.Mujakjung_be.global.board.dto.BoardDto;
import com.it.Mujakjung_be.global.board.entity.BoardEntity;
import com.it.Mujakjung_be.global.comment.dto.CommentResponseDto;
import com.it.Mujakjung_be.global.comment.entity.CommentEntity;
import com.it.Mujakjung_be.global.comment.repository.CommentRepository;
import com.it.Mujakjung_be.global.member.entity.MemberEntity;
import com.it.Mujakjung_be.global.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repository;
    private final MemberRepository memberRepository;

    // 댓글 목록 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> getComment(Long boardId) {
        return repository.findByBoardIdOrderByRegDateDesc(boardId)
                .stream()
                .map(CommentResponseDto::new)
                .toList();
    }

    // 댓글 저장
    @Transactional
    public void saveComment(CommentEntity comment, String email) {
        MemberEntity member =  memberRepository.findByEmail(email).orElseThrow(()-> new IllegalArgumentException("회원을 찾을 수 없습니다"));
        comment.setMemeber(member);
        repository.save(comment);
    }

    @Transactional
    public void updateComment(Long commentId, String content , String email){
       // 수정할 댓글 조회
       CommentEntity com = repository.findById(commentId).orElseThrow(()-> new IllegalArgumentException("댓글을 찾을 수 없습니다"));
       // 작성자 확인
        if(com.getMember() == null || !com.getMember().getEmail().equals(email)){
            throw  new org.springframework.security.access.AccessDeniedException("본인만 댓글을 작성 할 수 있습니다");
        }
        // 댓글 내용 수정
        com.updataContent(content);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, String email){
        // 삭제할 댓글 조회
        CommentEntity comment = repository.findById(commentId).orElseThrow(()-> new IllegalArgumentException("댓글을 찾을 수 없습니다"));
        if(comment.getMember() ==  null || !comment.getMember().getEmail().equals(email) ){
            throw  new AccessDeniedException("본인만의 댓글만 삭제 할 수 있습니다 ");
        }
        // 댓글 삭제
        repository.delete(comment);
    }
}