package com.it.Mujakjung_be.gobal.cart.service; // seriver -> service 수정

import com.it.Mujakjung_be.gobal.cart.entity.Cart;
import com.it.Mujakjung_be.gobal.cart.repoitory.CartRepository;
import com.it.Mujakjung_be.gobal.cart.repository.CartRepository; // repoitory -> repository 수정
import com.it.Mujakjung_be.gobal.memeber.entity.MemberEntity;
import com.it.Mujakjung_be.gobal.memeber.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service; // 추가
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Member;
import java.util.Optional;

@Service // @Setter 대신 @Service 추가!
@RequiredArgsConstructor
public class CartService {

    private final MemberRepository memberRepository;
    private final com.it.Mujakjung_be.gobal.cart.repository.CartRepository cartRepository;

    @Transactional
    public void addCart(String email, String productName, int price, int quantity) {
        // 회원 존재 여부 확인
        MemberEntity member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다"));

        // 이미 장바구니에 해당 상품이 있는지 체크
        // CartRepository에서도 이 메소드 이름이랑 똑같이 정의되어 있어야 해!
        Optional<Cart> existingCart = cartRepository.findByMemberEmailAndProductName(email, productName);

        if (existingCart.isPresent()) {
            // 있다면 수량 합산
            Cart cart = existingCart.get();
            cart.setQuantity(cart.getQuantity() + quantity);
            // JPA의 '변경 감지(Dirty Checking)' 덕분에 save를 따로 안 해도 수량이 업데이트돼!
        } else {
            // 없다면 새로 생성
            Cart cart = new Cart();
            cart.setMember(member);
            cart.setProductName(productName);
            cart.setProductPrice(price);
            cart.setQuantity(quantity);
            cartRepository.save(cart);
        }
    }
}