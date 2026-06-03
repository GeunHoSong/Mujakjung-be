package com.it.Mujakjung_be.global.travel.dto;

import com.it.Mujakjung_be.global.travel.entity.TravelEntity;
import lombok.*;

@Data
@Getter
@Setter
public class TravelDTO {
    private Long id;
    private String category;
    private String title;
    private String location;
    private String content;
    private int price;


    // 기본 생성자 (jackson 라이브러리 json를 객체를 바꿀때 필요)
    public static TravelDTO fromEntity (TravelEntity travel){
        TravelDTO dto = new TravelDTO();
        dto.setId(travel.getId());
        dto.setCategory(travel.getCategory());
        dto.setTitle(travel.getTitle());
        dto.setLocation(travel.getLocation());
        dto.setContent(travel.getContent());
        dto.setPrice(travel.getPrice());
        return dto;
    }






}
