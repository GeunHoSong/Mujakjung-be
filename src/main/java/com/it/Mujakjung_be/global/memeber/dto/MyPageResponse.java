package com.it.Mujakjung_be.global.memeber.dto;

import com.it.Mujakjung_be.global.memeber.entity.Address;
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
