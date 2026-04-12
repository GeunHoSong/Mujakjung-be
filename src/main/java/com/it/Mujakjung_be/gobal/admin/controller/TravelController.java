package com.it.Mujakjung_be.gobal.admin.controller;

import com.it.Mujakjung_be.gobal.admin.dto.TravelDTO;
import com.it.Mujakjung_be.gobal.admin.service.TravelService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TravelController {

    private final TravelService service;

    @PostMapping("/travel/register")
    public ResponseEntity<String> register(@RequestBody TravelDTO dto){
        service.registerTravel(dto);
        return ResponseEntity.ok(dto.getTitle() +"정보가 성공적으로 저장이 되엇습니다");
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
