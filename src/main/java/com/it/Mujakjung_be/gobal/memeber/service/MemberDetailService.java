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

@Service

public class MemberDetailService implements  UserDetailsService{
    private final MemberRepository repository ;

    public MemberDetailService(MemberRepository repository){
        this.repository  = repository;
    }


    @Override
    public UserDetails loadUserByUsername (String email) throws UsernameNotFoundException {

        MemberEntity entity = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        return new org.springframework.security.core.userdetails.User(
                entity.getEmail(),
                entity.getPassword(),
                Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_" + entity.getRole().name())
                )
        );
    }


}
