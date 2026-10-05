package com.it.Mujakjung_be.global.travel.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TravelAIResponseDto {
    private String emotion;         // 감정 분석 결과
    private String comfortMessage;  // 위로 메시지
    private List<ItineraryItem> itinerary; // Day별 일정 리스트

    public static class ItineraryItem {
        private int day;
        private String title;
        private String desc;

        public ItineraryItem(int day , String title , String desc) {
            this.day= day;
            this.title = title;
            this.desc = desc;
        }
    }
}
