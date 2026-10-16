package com.it.Mujakjung_be.global.reservation.service;

import com.it.Mujakjung_be.global.reservation.dto.ReservationDto;
import com.it.Mujakjung_be.global.reservation.entity.Reservation;
import com.it.Mujakjung_be.global.reservation.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;


    public Long registerReservation(ReservationDto dto) {
        Reservation reservation = new Reservation();

        reservation.setProductName(dto.getProductName());
        reservation.setDepartureDate(dto.getDepartureDate());
        reservation.setHeadCount(dto.getHeadCount());
        reservation.setMemo(dto.getMemo());
        reservation.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");

        // reservation이 아니라 repository로 save 호출!
        Reservation saved = repository.save(reservation);

        return saved.getId();
    }

}
