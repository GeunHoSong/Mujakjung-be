package com.it.Mujakjung_be.global.reservation.repository;

import com.it.Mujakjung_be.global.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
