package com.it.Mujakjung_be.inquiry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.Mujakjung_be.global.inquiry.dto.InquiryRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class InquiryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Test
    @DisplayName("1:1 문의 등록 성공 테스트")
    void submitInquiryTest() throws Exception {

        InquiryRequestDto dto = new InquiryRequestDto();

        dto.setEmail("test@text.com");
        dto.setTitle("작성자1");
        dto.setContent("테스트 중입니다");

        mvc.perform(post("/api/inquiry")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk()) // status() -> status().isOk() 로 수정
                .andExpect(content().string("문의가 성공적으로 접수되어 DB에 저장되었습니다!")); // 컨트롤러 리턴 메시지와 일치시킴
    }
}