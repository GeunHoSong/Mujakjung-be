package com.it.Mujakjung_be.global.comment.controller;

import com.it.Mujakjung_be.global.comment.entity.CommentEntity;
import com.it.Mujakjung_be.global.comment.service.CommentService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    @GetMapping("/{boardId}")
    public ResponseEntity<List<CommentEntity>> getComments(@PathVariable Long id){
        return ResponseEntity.ok(service.getComment(id));

    }


}
