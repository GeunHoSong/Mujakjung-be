package com.it.Mujakjung_be.global.comment.controller;

import com.it.Mujakjung_be.global.comment.dto.CommentResponseDto;
import com.it.Mujakjung_be.global.comment.entity.CommentEntity;
import com.it.Mujakjung_be.global.comment.service.CommentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    @PostMapping
    public ResponseEntity<String> saveComment(
            @RequestBody CommentEntity comment,
            Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                authentication.getPrincipal() instanceof String) {
            return ResponseEntity.status(401).body("로그인이 필요합니다.");
        }

        String email = authentication.getName();

        service.saveComment(comment, email);

        return ResponseEntity.ok("댓글 저장 성공");
    }


    @GetMapping("/{boardId}")
    public ResponseEntity<List<CommentResponseDto>> getComments(
            @PathVariable("boardId") Long boardId) {

        List<CommentResponseDto> comments = service.getComment(boardId);

        return ResponseEntity.ok(comments);
    }

    @PutMapping("/update/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId , @RequestBody Map<String , String > request , Authentication au){
       if(au == null || !au.isAuthenticated()|| au.getPrincipal() instanceof String ){
           return ResponseEntity.status(401).body("로그인이 필요 합니다");
       }
       service.updateComment(commentId, request.get("content"), au.getName());
       return ResponseEntity.ok("댓글 수정 완료");
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable("commentId") Long commentId , Authentication au){
        if(au == null || au.getPrincipal() instanceof  String ){
            return ResponseEntity.status(401).body("로그인이 필요 합니다");
        }
        service.deleteComment(commentId, au.getName());
         return ResponseEntity.ok("삭제 완료 되었습니다");

    }


}
