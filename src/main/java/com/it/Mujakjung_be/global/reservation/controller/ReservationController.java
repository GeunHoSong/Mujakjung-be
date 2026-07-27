package com.it.Mujakjung_be.global.reservation.controller;

import com.it.Mujakjung_be.global.reservation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ReservationController {

    private ReservationService service;
}
