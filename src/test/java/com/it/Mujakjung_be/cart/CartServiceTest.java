package com.it.Mujakjung_be.cart;

import com.it.Mujakjung_be.gobal.memeber.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // 테스트가 끝나면 DB를 깨끗하게 비워줘서 안심해도 돼!
public class CartServiceTest {
    @Autowired
    private com.it.Mujakjung_be.gobal.cart.service.CartService service;

    @Autowired
    private MemberRepository repository;

    @Autowired
    private com.it.Mujakjung_be.gobal.cart.repository.CartRepository cartRepository;
}
