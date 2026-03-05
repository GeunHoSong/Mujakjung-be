package com.it.Mujakjung_be.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.it.Mujakjung_be.gobal.memeber.dto.JoinRequest;
import com.it.Mujakjung_be.gobal.memeber.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// ✅ JUnit assert (지금 코드에선 안 쓰면 지워도 됨)
import static org.junit.jupiter.api.Assertions.assertTrue;

// ✅ MockMvc로 HTTP 요청 만들 때 사용하는 static import
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

// ✅ MockMvc 결과 검증용 static import
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * ✅ Member API 통합 테스트
 *
 * - @SpringBootTest : 스프링 부트 애플리케이션 컨텍스트를 "진짜로" 띄운다.
 *   (컨트롤러, 서비스, 레포지토리, 시큐리티 등 전체가 거의 실제처럼 동작)
 *
 * - @AutoConfigureMockMvc : MockMvc를 자동으로 등록해준다.
 *   (서버를 실제로 띄우지 않아도, HTTP 요청/응답 흐름을 테스트할 수 있게 해줌)
 */
@SpringBootTest
@AutoConfigureMockMvc
class MemberApiTest {

    /**
     * ✅ MockMvc
     * - 가짜 HTTP 클라이언트 역할
     * - 컨트롤러(/api/member/join)로 POST 요청을 보내고
     * - 응답 상태코드/본문 등을 검증할 수 있다.
     */
    @Autowired
    private MockMvc mockMvc;

    /**
     * ✅ ObjectMapper
     * - 자바 객체(JoinRequest)를 JSON 문자열로 바꿔주는 도구
     * - ex) om.writeValueAsString(request) -> {"email":"...","password":"...","name":"..."}
     */
    @Autowired
    private ObjectMapper om;

    /**
     * ⚠️ 현재 코드에서는 사용하지 않음 + @Autowired도 없음
     * - 이렇게 두면 repository는 null 상태임
     * - 만약 테스트 전에 DB를 비우고 싶다면 아래처럼 바꿔야 함:
     *
     *   @Autowired
     *   private MemberRepository repository;
     */
    private MemberRepository repository;

    /**
     * ✅ 회원가입 성공 테스트(의도)
     *
     * - /api/member/join 으로 POST 요청을 보낸다.
     * - 응답이 200 OK 인지 확인한다.
     *
     * ⚠️ 주의:
     * - 지금 email이 "test@test.com"으로 고정이라,
     *   DB에 이미 해당 이메일이 있으면 400이 떨어질 수 있음.
     * - 테스트를 항상 안정적으로 하려면 email을 유니크하게 만드는 게 좋다.
     */
    @Test
    void joinTest() throws Exception {
        // ✅ 요청 DTO 생성
        JoinRequest request = new JoinRequest();
        request.setEmail("test@test.com");
        request.setPassword("test1234!");
        request.setName("하수정");

        // ✅ MockMvc로 POST 요청 실행
        mockMvc.perform(
                        post("/api/member/join")                         // POST /api/member/join
                                .contentType(MediaType.APPLICATION_JSON) // 요청 본문이 JSON임을 명시
                                .content(om.writeValueAsString(request)) // JoinRequest 객체 -> JSON 문자열 변환 후 바디로 넣기
                )
                // ✅ 기대값: 성공하면 200 OK
                .andExpect(status().isOk());
    }

    /**
     * ✅ 중복 이메일 가입 테스트(의도)
     *
     * - 같은 email로 가입 요청을 했을 때,
     *   서비스 로직에서 "이미 가입된 이메일" 예외를 던지고
     *   컨트롤러/예외처리(@RestControllerAdvice 또는 try/catch)가
     *   400 BadRequest로 바꿔서 내려주는지 확인한다.
     *
     * ⚠️ 이 테스트가 "항상" 통과하려면:
     * 1) 먼저 같은 email이 DB에 저장되어 있어야 함(이미 가입된 상태)
     *    - 방법 A: 테스트 시작 전에 DB에 미리 insert
     *    - 방법 B: 테스트 안에서 1번 가입 성공(200) 시킨 뒤, 2번째 요청으로 400 검증 (정석)
     *
     * 2) 현재는 "save_test@test.com"이 DB에 이미 있어야 400이 나옴
     */
    @Test
    void join_saves_member() throws Exception {
        // given (테스트 준비)
        String email = "save_test@test.com";

        JoinRequest request = new JoinRequest();
        request.setEmail(email);
        request.setPassword("test1234!");
        request.setName("하수정");

        // when + then (요청 실행 + 결과 검증)
        mockMvc.perform(
                        post("/api/member/join")
                                .contentType(MediaType.APPLICATION_JSON)
                                // ✅ 핵심: JoinRequest 객체를 JSON으로 변환해서 넣어야 함
                                // - 예전에 .content(json) 처럼 json 변수를 넣었는데
                                //   json 변수가 선언되어 있지 않아서 에러가 났던 것!
                                .content(om.writeValueAsString(request))
                )
                // ✅ 기대값: 중복이면 400 Bad Request
                .andExpect(status().isBadRequest())
                // ✅ 기대값: 응답 바디에 에러 메시지가 들어오는지 확인
                // (현재 응답이 text/plain이라서 content().string(...)으로 검사하는 게 맞음)
                .andExpect(content().string("이미 가입된 이메일 입니다"));
    }
}

