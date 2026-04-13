package com.it.Mujakjung_be.global.memeber.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class LoginResponse {
    private String token;
    private String role;
    private String name;


    public LoginResponse (){

    }

    public LoginResponse(String token, String role, String name){
        this.token = token;
        this.role = role;
        this.name = name;
    }


    public String getToken (){
        return token;
    }

    public void setToken(){
        this.token= token;
    }

    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setName(String name) {
        this.name = name;
    }
}
