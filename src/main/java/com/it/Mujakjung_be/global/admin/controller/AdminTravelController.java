package com.it.Mujakjung_be.global.admin.controller;

import com.it.Mujakjung_be.global.travel.dto.TravelDTO;
import com.it.Mujakjung_be.global.travel.service.TravelService;
import com.it.Mujakjung_be.global.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminTravelController {

    private final TravelService  travelService;
    private final MemberService memberService;

    // 상품 등록
    @PostMapping("/register")
    public ResponseEntity<TravelDTO> register(@RequestBody TravelDTO dto) {
        return ResponseEntity.ok(travelService.registerTravel(dto));
    }

    // 상품 정보 수정
    @PostMapping("/{id}")
    public ResponseEntity<TravelDTO> update(
            @PathVariable Long id,
            @ModelAttribute TravelDTO dto,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) {
        TravelDTO updatedDto = travelService.updateTravel(id, dto, file);
        return ResponseEntity.ok(updatedDto);
    }

//    // 상품 삭제
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable Long id) {
//        travelService.deleteTravel(id);
//        return ResponseEntity.noContent().build();
//    }

    // 상품 목록 조회 예시
    @GetMapping("/list")
    public ResponseEntity<List<TravelDTO>> list() {
        return ResponseEntity.ok(travelService.getAllTravels());
    }
}