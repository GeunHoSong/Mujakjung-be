
package com.it.Mujakjung_be.member;

import com.it.Mujakjung_be.gobal.memeber.dto.JoinRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

// ✅ Boot 4.x: 패키지가 바뀜 (여기가 핵심)
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static aQute.bnd.annotation.headers.Category.json;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

/*
 SpringBootTest = 애플리케이션을 "통째로" 띄우는 통합 테스트
 - Controller + Service + Repository + (설정에 따라) DB 연결까지 전부 로딩됨
*/
@SpringBootTest(
        // MOCK = 톰캣 같은 실제 서버는 안 띄우고, 서블릿 환경만 흉내내서 테스트
        webEnvironment = SpringBootTest.WebEnvironment.MOCK
)

/*
 AutoConfigureMockMvc = MockMvc를 스프링이 자동으로 만들어서 빈으로 등록해줌
 - 그래서 아래 @Autowired MockMvc가 가능해짐
*/

@AutoConfigureMockMvc
class MemberApiTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper om;


    @Test
    void joinTest() throws Exception {
        JoinRequest request = new JoinRequest();
        request.setEmail("test@test.com");
        request.setPassword("test9876!");
        request.setName("하수정");
        mockMvc.perform(post("/api/member/join")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                                .content(om.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

}