package com.it.Mujakjung_be.global.comment.controller;

import com.it.Mujakjung_be.global.comment.entity.CommentEntity;
import com.it.Mujakjung_be.global.comment.service.CommentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    @PostMapping
    public ResponseEntity<String> saveComment(@RequestBody CommentEntity comment){
        service.saveComment(comment);
        return ResponseEntity.ok("댓글 저장 성공");
    }


    @GetMapping("/{boardId}")
    public ResponseEntity<List<CommentEntity>> getComments(@PathVariable Long id){
        List<CommentEntity> comment = service.getComment(id);
        return ResponseEntity.ok(comment);
    }


}
