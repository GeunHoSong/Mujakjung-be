package com.it.Mujakjung_be.gobal.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TravelDTO {

    private String category;
    private String title;
    private String location;
    private String content;

    // 기본 생성자 (jackson 라이브러리 json를 객체를 바꿀때 필요)
}
