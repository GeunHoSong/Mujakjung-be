package com.it.Mujakjung_be.admin;

import com.it.Mujakjung_be.global.travel.dto.TravelDTO;
import com.it.Mujakjung_be.global.travel.service.TravelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TravelServiceTest {

    @Autowired
    private TravelService service;

    @Test
    @DisplayName("여행지 등록 하면 id 자동으로 생겨야 한다")
    void registerTest() {
        // 1. 준비
        TravelDTO dto = new TravelDTO();
        dto.setCategory("domestic");
        dto.setTitle("테스트용 문학 경기장");
        dto.setLocation("인천");
        dto.setContent("수정이랑 같이 가자");

        // 2. 실행
        TravelDTO result = service.registerTravel(dto);

        // 3. 검증 (주석 풀고 제대로 확인해보자!)
        assertThat(result.getId()).isNotNull();
        System.out.println("✅ 생성된 아이디 번호: " + result.getId());
    }
}