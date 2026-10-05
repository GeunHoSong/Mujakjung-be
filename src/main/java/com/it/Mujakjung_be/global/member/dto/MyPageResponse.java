package com.it.Mujakjung_be.global.member.dto;

import com.it.Mujakjung_be.global.member.entity.Address;
import lombok.Data;

@Data
public class MyPageResponse {
    private String email;
    private String name;
    private String phone;
    private String gender;
    private Address address;
    private String role;
}
