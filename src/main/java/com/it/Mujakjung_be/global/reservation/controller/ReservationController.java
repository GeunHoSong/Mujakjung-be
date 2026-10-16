package com.it.Mujakjung_be.global.reservation.controller;

import com.it.Mujakjung_be.global.reservation.dto.ReservationDto;
import com.it.Mujakjung_be.global.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReservationController {

    private  final ReservationService service;



    @PostMapping // 생성은 PostMapping
    public ResponseEntity<Long> createReservation(@RequestBody ReservationDto dto) {
        Long reservationId = service.registerReservation(dto);
        return ResponseEntity.ok(reservationId);
    }
}
