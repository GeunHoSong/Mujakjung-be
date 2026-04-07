package com.it.Mujakjung_be.gobal.cart.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CartRequestDto {

    private String email; // 사용자 식별을 위한 이메일
    private String productName; // 상품 이름
    private int price; // 상품 가격
    private int quantity; // 담을 수량
}
