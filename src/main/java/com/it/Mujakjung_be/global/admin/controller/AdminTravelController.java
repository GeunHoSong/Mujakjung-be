package com.it.Mujakjung_be.global.admin.controller;

import com.it.Mujakjung_be.global.travel.dto.TravelDTO;
import com.it.Mujakjung_be.global.travel.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/travels")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AdminTravelController {

    private final TravelService service;

    @PostMapping("/register") // 👈 최종 주소: /api/admin/travels/register
    public ResponseEntity<TravelDTO> register(@RequestBody TravelDTO dto) {
        return ResponseEntity.ok(service.registerTravel(dto));
    }

    // 싱품 정보 수정
    @PostMapping("/{id}")
    public ResponseEntity<TravelDTO> update(@PathVariable Long id ,@RequestBody TravelDTO dto){
        return ResponseEntity.ok(dto);
    }
    // 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<TravelDTO> delete (@PathVariable Long id ){
        return ResponseEntity.noContent().build();
    }
}
