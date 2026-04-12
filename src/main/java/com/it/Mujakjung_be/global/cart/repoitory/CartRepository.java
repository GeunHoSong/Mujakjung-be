// CartRepository.java
package com.it.Mujakjung_be.global.cart.repository;

import com.it.Mujakjung_be.global.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// 제네릭 첫 번째는 엔티티 클래스명(Cart), 두 번째는 @Id의 타입(Long)이야!
public interface CartRepository extends JpaRepository<Cart, Long> {

    // 이 메서드 이름이 Service에서 호출하는 이름과 정확히 일치해야 해
    Optional<Cart> findByMemberEmailAndProductName(String email, String productName);
}