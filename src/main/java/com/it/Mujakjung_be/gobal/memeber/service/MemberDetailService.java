package com.it.Mujakjung_be.gobal.memeber.service;

import com.it.Mujakjung_be.gobal.memeber.entity.MemberEntity;
import com.it.Mujakjung_be.gobal.memeber.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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

    /**
     * 시큐리티가 로그인을 시도할 때 내부적으로 호출하는 메서드
     * @param email 사용자가 입력한 아이디(이메일)
     * @return 시큐리티가 이해할 수 있는 형태의 사용자 정보(UserDetails)
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1. DB에서 이메일로 사용자 찾기
        MemberEntity entity = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email));

        // 2. 찾은 회원 정보를 시큐리티 전용 'User' 객체에 담아서 반환
        return new org.springframework.security.core.userdetails.User(
                entity.getEmail(),    // 아이디
                entity.getPassword(), // 암호화된 비밀번호
                Collections.singletonList(
                        // 3. 권한 설정 (예: ROLE_USER)
                        new SimpleGrantedAuthority("ROLE_" + entity.getRole().name())
                )
        );
    }
}