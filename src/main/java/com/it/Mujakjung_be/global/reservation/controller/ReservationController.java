package com.it.Mujakjung_be.global.reservation.controller;

import com.it.Mujakjung_be.global.reservation.dto.ReservationDto;
import com.it.Mujakjung_be.global.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReservationController {

    private ReservationService service;


    @GetMapping
    public ResponseEntity<Long> createReservation(@RequestBody ReservationDto dto) {
        Long l = service.registerReservation(dto);
        return ResponseEntity.ok(l);
    }
}
