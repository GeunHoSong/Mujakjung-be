package com.it.Mujakjung_be.gobal.cart.entity;

import com.it.Mujakjung_be.gobal.memeber.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.lang.reflect.Member;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    // 회원 엔티티 와 직접 연결 (가장 중요)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email", referencedColumnName = "email")
    private MemberEntity member;

    // 상품 정보
    @Column(nullable = false)
    private String productName;

    private int productPrice; // 상품 가격

    private int quantity = 1;

    // 데이터 생성시 시간 자동 반복
    private LocalDateTime addedAt;


    @PrePersist
    public void prePersist(){
        this.addedAt = LocalDateTime.now();
    }







}
