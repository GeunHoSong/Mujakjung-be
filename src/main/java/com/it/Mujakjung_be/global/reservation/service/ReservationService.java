package com.it.Mujakjung_be.global.reservation.service;

import com.it.Mujakjung_be.global.reservation.dto.ReservationDto;
import com.it.Mujakjung_be.global.reservation.entity.Reservation;
import com.it.Mujakjung_be.global.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private ReservationRepository repository;


    public Long registerReservation(ReservationDto dto) {
        Reservation reservation = new Reservation();

        reservation.setTravelId(dto.getTravelId());
        reservation.setStatus("PENDING");
        reservation.setSpecialRequest(dto.getSpecialRequest());

        Reservation save = repository.save(reservation);

        return  save.getId();


    }

}
