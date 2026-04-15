package com.it.Mujakjung_be.global.travel.controller;
import com.it.Mujakjung_be.global.travel.dto.TravelDTO;
import com.it.Mujakjung_be.global.travel.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travels") // 기본 주소: /api/travels
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TravelController {

    private final TravelService service;

    // 🚩 [관리자] 등록 (주소: /api/travels/admin/register)
    // 보안을 위해 어드민용은 뒤에 admin을 붙여주는게 좋아
    @PostMapping("/admin/register")
    public ResponseEntity<TravelDTO> register(@RequestBody TravelDTO dto) {
        TravelDTO savedDto = service.registerTravel(dto);
        return ResponseEntity.ok(savedDto);
    }

    // 🚩 [일반유저] 전체 목록 (주소: /api/travels)
    // 리액트 fetch("http://localhost:8080/api/travels") 와 정확히 일치하게!
    @GetMapping
    public ResponseEntity<List<TravelDTO>> findAll(){
        return ResponseEntity.ok(service.getAllTravels());
    }

    // 🚩 [일반유저] 상세 조회 (주소: /api/travels/5)
    @GetMapping("/{id}")
    public ResponseEntity<TravelDTO> findById(@PathVariable Long id){
        TravelDTO travelDetail = service.getTravelDetail(id);
        return ResponseEntity.ok(travelDetail);
    }
}