package com.it.Mujakjung_be.global.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import org.apache.coyote.Response;

@Getter
@Setter
public class NaverDto {
    private String id;
    private String email;
    private String name;



}
