package com.it.Mujakjung_be.global.cart.controller;

import com.it.Mujakjung_be.global.cart.dto.CartRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final com.it.Mujakjung_be.global.cart.service.CartService service;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartRequestDto dto){
        service.addCart(
                dto.getEmail(),
                dto.getProductName(),
                dto.getPrice(),
                dto.getQuantity()
        );
        return ResponseEntity.ok("장바구니에 담았습니다");
    }
}
