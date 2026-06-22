package com.it.Mujakjung_be.global.admin.controller;

import com.it.Mujakjung_be.global.member.dto.MemberResponse;
import com.it.Mujakjung_be.global.member.service.MemberService;
import com.it.Mujakjung_be.global.travel.dto.TravelDTO;
import com.it.Mujakjung_be.global.travel.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor

public class AdminTravelController {

    private final TravelService service;
    private final MemberService memberService;

    @PostMapping("/register") // 👈 최종 주소: /api/admin/travels/register
    public ResponseEntity<TravelDTO> register(@RequestBody TravelDTO dto) {
        return ResponseEntity.ok(service.registerTravel(dto));
    }

    // 싱품 정보 수정
    @PostMapping("/{id}")
    public ResponseEntity<TravelDTO> update(
            @PathVariable Long id,
            @ModelAttribute TravelDTO dto, // @RequestBody를 @ModelAttribute로 바꿔야 함
            @RequestParam(value = "file", required = false) MultipartFile file) { // 파일 추가

        // 이제 서비스에서 파일(file)과 데이터(dto)를 같이 처리할 수 있게 됨
        TravelDTO updatedto = service.updateTravel(id, dto, file);
        return ResponseEntity.ok(updatedto);
    }
    // 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<TravelDTO> delete (@PathVariable Long id ){
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/list")
    public ResponseEntity<List<TravelDTO>> list(){
        return ResponseEntity.ok(service.getAllTravels());
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponse>> getMemberList(){
        return ResponseEntity.ok(memberService.finllAll());
    }
}
