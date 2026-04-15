package com.it.Mujakjung_be.global.cart.service;

import com.it.Mujakjung_be.global.cart.entity.Cart;
import com.it.Mujakjung_be.global.cart.repository.CartRepository;
import com.it.Mujakjung_be.global.member.entity.MemberEntity;
import com.it.Mujakjung_be.global.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service // 스프링이 이 클래스를 '서비스'로 인식하게 해줌 (비즈니스 로직 담당)
@RequiredArgsConstructor // final이 붙은 필드(리포지토리 등)를 자동으로 생성자 주입해줌
public class CartService {

    // DB에 접근하기 위한 리포지토리들 (final 필수!)
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;

    /**
     * 장바구니에 상품을 추가하는 핵심 메서드
     */
    @Transactional // 메서드 안의 모든 작업이 하나로 묶임 (하나라도 실패하면 롤백!)
    public void addCart(String email, String productName, int price, int quantity) {

        // 1. 회원 검증: DB에 이메일로 등록된 사용자가 있는지 확인
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다"));

        // 2. 중복 체크: 이 회원의 장바구니에 이미 같은 상품이 담겨 있는지 확인
        // findByMemberEmailAndProductName은 우리가 리포지토리에 직접 만든 메서드!
        Optional<Cart> existingCart = cartRepository.findByMemberEmailAndProductName(email, productName);

        if (existingCart.isPresent()) {
            // [상황 A] 이미 장바구니에 상품이 있다면?
            Cart cart = existingCart.get();
            // 기존 수량에 새로 추가한 수량을 더함 (예: 1개 있었는데 2개 더하면 3개로!)
            cart.setQuantity(cart.getQuantity() + quantity);

            // 💡 꿀팁: JPA의 '변경 감지(Dirty Checking)' 덕분에
            // 따로 cartRepository.save(cart)를 안 써도 메서드가 끝나면 자동으로 DB에 업데이트돼!
        } else {
            // [상황 B] 장바구니에 처음 담는 상품이라면?
            Cart cart = new Cart(); // 새로운 장바구니 객체 생성
            cart.setMember(member); // 누구의 장바구니인지 연결
            cart.setProductName(productName); // 상품명 저장
            cart.setProductPrice(price);      // 가격 저장
            cart.setQuantity(quantity);       // 수량 저장

            // 새로 만든 건 DB에 명시적으로 저장해줘야 함!
            cartRepository.save(cart);
        }
    }
}