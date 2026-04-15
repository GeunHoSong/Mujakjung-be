package com.it.Mujakjung_be.global.member.service;

import com.it.Mujakjung_be.global.member.entity.MemberEntity;
import com.it.Mujakjung_be.global.member.repository.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * 스프링 시큐리티의 전용 서비스!
 * 사용자의 아이디(이메일)를 가지고 DB에서 회원 정보를 가져와 시큐리티 전용 객체로 변환해줌
 */
@Service
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository repository;

    // 생성자 주입 (의존성 주입)
    public MemberDetailService(MemberRepository repository) {
        this.repository = repository;
    }

    // MemberDetailService.java
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberEntity entity = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));

        return new org.springframework.security.core.userdetails.User(
                entity.getEmail(),
                entity.getPassword(),
                Collections.singletonList(
                        // DB의 ADMIN을 "ROLE_ADMIN"으로 변환해서 시큐리티에 보고
                        new SimpleGrantedAuthority("ROLE_" + entity.getRole().name())
                )
        );
    }
}