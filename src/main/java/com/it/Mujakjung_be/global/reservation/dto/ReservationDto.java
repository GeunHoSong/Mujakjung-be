package com.it.Mujakjung_be.global.reservation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ReservationDto {
    private Long id;
    private Long travelId;
    private String status;
    private String specialRequest;
}
