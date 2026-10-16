package com.it.Mujakjung_be.global.reservation.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ReservationDto {
    private Long id;
    private String productName;
    private String departureDate; // 프론트엔드와 이름 맞춤
    private int headCount;
    private String memo;
    private String status;


}
