package com.it.Mujakjung_be.global.notice.contoller;

import com.it.Mujakjung_be.global.notice.dto.NoticeDto;
import com.it.Mujakjung_be.global.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final NoticeService service;

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // 👈 관리자만 실행 가능
    public ResponseEntity<String> save(@RequestBody NoticeDto dto){
        service.saveNotice(dto);
        return ResponseEntity.ok("등록 성공");
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // 👈 관리자만 실행 가능
    public ResponseEntity<String> update(@PathVariable Long id , @RequestBody NoticeDto dto){
        service.updateNotice(id , dto);
        return ResponseEntity.ok("수정 완료");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')") // 👈 관리자만 실행 가능
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.ok("삭제 성공");
    }

    // 조회 메서드들은 @PreAuthorize가 없으므로 누구나 접근 가능!
    @GetMapping("/list")
    public ResponseEntity<List<NoticeDto>> list(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoticeDto> detail(@PathVariable Long id){
        NoticeDto noticeDto = service.findById(id);

        return ResponseEntity.ok(noticeDto);
    }
}