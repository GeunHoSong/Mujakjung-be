package com.it.Mujakjung_be.global.notice.contoller;

import com.it.Mujakjung_be.global.notice.dto.NoticeDto;
import com.it.Mujakjung_be.global.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/list")
    public ResponseEntity<List<NoticeDto>> list(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDto> detail(@PathVariable Long id){
        return ResponseEntity.ok(service.findById(id));
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id , @RequestBody NoticeDto dto){
        service.updateNotice(id , dto);
        return ResponseEntity.ok("수정 완료");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteById(id); // 여기도 deleteById로 변경!
        return ResponseEntity.ok("삭제 성공");
    }
}
