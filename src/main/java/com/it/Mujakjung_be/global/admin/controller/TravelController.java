package com.it.Mujakjung_be.global.admin.controller;

import com.it.Mujakjung_be.global.admin.dto.TravelDTO;
import com.it.Mujakjung_be.global.admin.service.TravelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
// 1. 오타 수정: location -> localhost로, 세미콜론(:) 대신 슬래시(//) 사용
@CrossOrigin(origins = "http://localhost:5173")
public class TravelController {

    private final TravelService service;

    @PostMapping("/travel/register")
    // 2. @RequestBody 추가: 리액트(axios)에서 보내는 JSON 데이터를 자바 객체로 변환해줌 (이거 없으면 데이터가 다 비어서 들어옴!)
    public ResponseEntity<TravelDTO> register(@RequestBody TravelDTO dto) {
        // 서비스에서 ID가 채워진 DTO를 받아옴
        TravelDTO savedDto = service.registerTravel(dto);

        // 객체 그대로 리턴! (이제 리액트에서 res.data.id 접근 가능)
        return ResponseEntity.ok(savedDto);
    }

    @GetMapping("/travel/{id}")
    public ResponseEntity<TravelDTO> findById(@PathVariable Long id){
        TravelDTO travelDetail = service.getTravelDetail(id);
        return ResponseEntity.ok(travelDetail);
    }

    @GetMapping("/travel/list")
    public ResponseEntity<List<TravelDTO>> findAll(){
        return ResponseEntity.ok(service.getAllTravels());
    }
}