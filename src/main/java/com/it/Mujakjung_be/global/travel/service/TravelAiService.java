package com.it.Mujakjung_be.global.travel.service;

import com.it.Mujakjung_be.global.travel.dto.TravelAiRequestDto;
import com.it.Mujakjung_be.global.travel.dto.TravelAIResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelAiService {

    private final RestClient restClient = RestClient.create();

    @Value("${gemini.api.key}")
    private String geminiApiKey;

    public TravelAIResponseDto getRecommendation(TravelAiRequestDto requestDto) {
        TravelAIResponseDto response = new TravelAIResponseDto();

        if (requestDto.getDestination() != null && !requestDto.getDestination().isEmpty()) {
            response.setEmotion(requestDto.getDestination() + " 여행을 통한 감성 치유 🌿");
            response.setComfortMessage("요청하신 " + requestDto.getDestination() + "(으)로의 여행을 통해 깊은 휴식을 얻실 수 있도록 일정을 준비했어요.");
        } else {
            response.setEmotion("지친 마음의 휴식과 힐링 필요 🌊");
            response.setComfortMessage("요청하신 \"" + requestDto.getPrompt() + "\"에 맞춰, 마음을 차분히 가라앉힐 수 있는 최적의 여행지를 골라봤습니다.");
        }

        response.setItinerary(List.of(
                new TravelAIResponseDto.ItineraryItem(1, "오션뷰 카페에서 멍 때리기 & 산책", "조용한 공간에서 따뜻한 차와 함께 온전히 사색에 잠기기"),
                new TravelAIResponseDto.ItineraryItem(2, "자연 속 힐링 산책로 걷기", "피톤치드 가득한 숲길을 걸으며 복잡한 머릿속을 비우고 재충전하기")
        ));

        return response;
    }
}