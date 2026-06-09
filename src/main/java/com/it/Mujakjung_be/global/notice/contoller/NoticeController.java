package com.it.Mujakjung_be.global.notice.contoller;

import com.it.Mujakjung_be.global.notice.dto.NoticeDto;
import com.it.Mujakjung_be.global.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService service;
    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody NoticeDto dto){
        service.saveNotice(dto);
        return ResponseEntity.ok("등록 성공");
    }
}
