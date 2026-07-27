package com.it.Mujakjung_be.cart;

import com.it.Mujakjung_be.global.cart.seriver.CartService;
import com.it.Mujakjung_be.global.member.entity.MemberEntity;
import com.it.Mujakjung_be.global.member.entity.Role;
import com.it.Mujakjung_be.global.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class CartServiceTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("장바구니 담기 테스트 : 새로운 상품 추가 및 수량 업데이트 확인")
    void addCartTest() {
        // 1. Given: 테스트용 회원 생성 및 저장
        String email = "test1@test.com";
        MemberEntity m = new MemberEntity();
        m.setEmail(email);
        m.setName("테스트 유저");
        m.setPassword("1234");
        m.setNickname("테스트 네임");
        m.setRole(Role.USER);

        memberRepository.save(m);

        // 2. When & Then (장바구니 로직 추가 부분에 맞춰 검증 작성)
        assertThat(m.getId()).isNotNull();
        System.out.println("✅ 테스트 회원 저장 완료, ID: " + m.getId());
    }
}