package com.it.Mujakjung_be.global.reservation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id;

    // 어떤 회원이 예약했는지 (Member 엔티티와 연관관계 매핑)
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "member_id")
    // private Member member;

    // 어떤 여행지/상품을 예약했는지 (Travel 등과 연관관계 매핑 필요시 추가)


    private Long travelId;

    private Long status;

    private String specialRequest; // 요청사항 등
}


