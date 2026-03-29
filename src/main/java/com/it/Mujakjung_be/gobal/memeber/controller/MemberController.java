package com.it.Mujakjung_be.gobal.memeber.controller;

import com.it.Mujakjung_be.gobal.memeber.dto.*;
import com.it.Mujakjung_be.gobal.memeber.repository.MemberRepository;
import com.it.Mujakjung_be.gobal.memeber.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
@CrossOrigin(origins = {"*"})
public class MemberController {
    // Service 계층 호출
    // 회원 관련 비즈니스 로직 처리
    private final MemberService service;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinRequest request){
        // @RequestBody
        // 클라이언트가 보낸 JSON 데이터를
        // JoinRequest 객체로 변환

        // 회원가입 로직 실행
        service.save(request);
        // 성공 응답 반환
        return ResponseEntity.ok("회원 가입 성공");
    }

    // 로그인 API
// 클라이언트(React)가 이메일과 비밀번호를 보내면
// 로그인 처리 후 JWT 토큰을 반환하는 API
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){

        // Service 계층에서 로그인 로직 실행
        // 이메일 존재 확인 + 비밀번호 검증 + JWT 생성
        LoginResponse response = service.login(request);

        // 로그인 성공 시 토큰을 JSON 형태로 반환
        return ResponseEntity.ok(response);
    }

    // 서버 정상 작동 확인용 테스트 API
// 브라우저에서 호출하면 단순 문자열 반환
    @GetMapping("/test")
    public String test(){
        return "테스트 성공";
    }
    // 로그인한 사용자의 정보를 조회하는 API
    @PostMapping("/mypage")
    public ResponseEntity<MyPageResponse> mypage(Authentication authentication) {

        // Spring Security에서 현재 로그인한 사용자 정보를 가져옴
        // JWT 필터가 인증 객체를 SecurityContext에 저장했기 때문에 사용 가능
        String email = authentication.getName();

        // 이메일을 기준으로 회원 정보를 조회
        // Service에서 DB 조회 후 DTO로 변환
        return ResponseEntity.ok(service.getMyPage(email));

    }
    // 프로필 업데이트 (닉네임, 사진, 자기소개 등)
    // PATCH는 리소스를 부분적으로 수정할 때 사용해!
    @PostMapping("/proflie")
    public ResponseEntity<String> updateProfile(Authentication authentication, @RequestBody ProfileRequest request ){
        String email = authentication.getName();
        log.info("프로필 업데이트 요청: {}", email);

        service.updateProfile(email, request);
        return ResponseEntity.ok("프로필 성공적으로 업데이트 되었습니다");
    }

}
