package com.it.Mujakjung_be.global.oauth2;

import com.it.Mujakjung_be.global.member.entity.MemberEntity;
import com.it.Mujakjung_be.global.member.entity.Role;
import com.it.Mujakjung_be.global.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository repository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 어떤 소셜 로그인인지 확인 (google, naver 등)
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        String email;
        String name;

        if ("naver".equals(registrationId)) {
            // 네이버는 응답 데이터가 'response'라는 맵 안에 들어있음!
            Map<String, Object> response = oAuth2User.getAttribute("response");
            if (response == null) {
                throw new OAuth2AuthenticationException("네이버 로그인 실패: response 정보를 찾을 수 없습니다.");
            }
            email = (String) response.get("email");
            name = (String) response.get("name");
        } else if ("google".equals(registrationId)) {
            email = oAuth2User.getAttribute("email");
            name = oAuth2User.getAttribute("name");
        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인입니다: " + registrationId);
        }

        if (email == null) {
            throw new OAuth2AuthenticationException("OAuth2 로그인 실패: 이메일 정보를 찾을 수 없습니다.");
        }

        // DB에 자동 회원가입(Upsert) 진행
        String finalName = (name != null) ? name : "소셜유저";
        MemberEntity member = repository.findByEmail(email).orElseGet(() -> {
            MemberEntity newMember = new MemberEntity();
            newMember.setEmail(email);
            newMember.setName(finalName);
            newMember.setRole(Role.USER);
            newMember.setPassword("SOCIAL_LOGIN_USER");
            return repository.save(newMember);
        });

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + member.getRole().name())),
                oAuth2User.getAttributes(),
                registrationId.equals("naver") ? "response" : "email"
        );
    }
}