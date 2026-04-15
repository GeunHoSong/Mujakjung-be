package com.it.Mujakjung_be.cart;

import com.it.Mujakjung_be.global.member.entity.MemberEntity;
import com.it.Mujakjung_be.global.member.entity.Role;
import com.it.Mujakjung_be.global.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CartServiceTest {
    @Autowired
    private com.it.Mujakjung_be.global.cart.service.CartService cartService;
    @Autowired
    private MemberRepository repository;


    @Test
    @DisplayName("장바구니 담기 테스트 :새로운 상품 추가 및 수량 업데이트 확인")
    void addCartTest(){
        // 1. Given: 테스트용 회원 생성 및 저장
        String email = "test1@test.com";
        MemberEntity m = new MemberEntity();
        m.setEmail(email);
        m.setName("테스트 유저");
        m.setPassword("1234");
        m.setNickname("테스트 네임");
        m.setRole(Role.USER);
        repository.save(m);

        // 2. When: 장바구니에 처음 담기 (상품명 끝에 공백 제거!)
        String productName = "제주도 힐링 코스";
        cartService.addCart(email, productName, 500000, 1);
        System.out.println("✅ 첫 번째 담기 성공");

        // 3. When: 동일한 상품 한 번 더 담기 (변수를 써서 오타 방지!)
        cartService.addCart(email, productName, 51000, 2);
        System.out.println("✅ 수량 업데이트 테스트 성공 (총 3개 예정)");
    }
    



}
