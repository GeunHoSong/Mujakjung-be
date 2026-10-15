package com.it.Mujakjung_be.global.member.dto;

import com.it.Mujakjung_be.global.member.entity.Address;
import lombok.*;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyPageResponse {
    private String email;
    private String name;
    private String phone;
    private String gender;
    private Address address;
    private String role;

}
