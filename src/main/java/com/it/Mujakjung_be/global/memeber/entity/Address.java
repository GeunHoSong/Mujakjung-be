package com.it.Mujakjung_be.global.memeber.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Address {
    private String zipcode; // 우편 주소
    private String city; // 기본 주소 (예 : 서울 특별시)
    private String street;// 상세 주소

    public Address(String zipcode, String city, String street) {
        this.zipcode= zipcode;
        this.city = city;
        this.street = street;
    }
}
