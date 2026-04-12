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
@CrossOrigin(origins = "http:")
public class TravelController {

    private final TravelService service;

    @PostMapping("/travel/register")
    public ResponseEntity<TravelDTO> register(TravelDTO dto) {
        // 서비스에서 ID가 채워진 DTO를 받아옴
        TravelDTO savedDto = service.registerTravel(dto);

        // 객체 그대로 리턴! (이제 리액트에서 res.data.id 접근 가능)
        return ResponseEntity.ok(savedDto);
    }
    @GetMapping("/travel/{id}")
    public ResponseEntity<TravelDTO> findById(@PathVariable  Long id){
        TravelDTO travelDetail = service.getTravelDetail(id);
        return ResponseEntity.ok(travelDetail);
    }

    @GetMapping("/travel/list")
    public ResponseEntity<List<TravelDTO>> findAll(){
        return ResponseEntity.ok(service.getAllTravels());
    }
}
