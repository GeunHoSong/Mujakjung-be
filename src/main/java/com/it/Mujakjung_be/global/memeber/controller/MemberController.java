package com.it.Mujakjung_be.global.memeber.controller;

import com.it.Mujakjung_be.global.memeber.dto.*;
import com.it.Mujakjung_be.global.memeber.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j // 로그 기록을 위한 어노테이션
@RestController // JSON 형태로 데이터를 주고받는 API 컨트롤러
@RequiredArgsConstructor // final 필드(service) 자동 주입
@RequestMapping("/api/member") // 공통 주소 설정
@CrossOrigin(origins = {"*"}) // 모든 도메인에서의 요청 허용 (CORS 에러 방지)
public class MemberController {

    private final MemberService service; // 비즈니스 로직을 담당하는 서비스 호출

    /**
     * 회원가입 API
     * POST /api/member/join
     */
    @PostMapping("/join")
    public ResponseEntity<String> join(
            @RequestPart("memberData") JoinRequest request, // 👈 프론트의 formData.append("memberData", ...)와 이름 통일!
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage // 👈 사진 파일 받기
    ){
        log.info("회원가입 요청: {}", request.getEmail());

        // 서비스단에 이미지 파일도 같이 넘겨주도록 수정해야 할 거야!
        service.save(request, profileImage);

        return ResponseEntity.ok("회원 가입 성공");
    }

    /**
     * 로그인 API
     * POST /api/member/login
     * 성공 시 JWT 토큰이 담긴 LoginResponse 반환
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        log.info("로그인 요청: {}", request.getEmail());

        // 이메일 확인 + 비번 검증 + JWT 생성 로직 실행
        LoginResponse response = service.login(request);

        return ResponseEntity.ok(response);
    }

    /**
     * 서버 정상 작동 테스트용
     * GET /api/member/test
     */
    @GetMapping("/test")
    public String test(){
        return "테스트 성공";
    }

    /**
     * 마이페이지 정보 조회 API
     * POST /api/member/mypage
     * Authentication: 시큐리티가 검증한 현재 로그인 유저 정보가 담겨 있음
     */
    @PostMapping("/mypage")
    public ResponseEntity<MyPageResponse> mypage(Authentication authentication) {
        // JWT 필터를 통해 저장된 인증 객체에서 이메일(아이디) 추출
        String email = authentication.getName();

        // 서비스에서 해당 이메일의 회원 정보를 가져와서 반환
        return ResponseEntity.ok(service.getMyPage(email));
    }

    /**
     * 프로필 업데이트 API (닉네임, 사진, 자기소개 등)
     * POST /api/member/proflie
     */
    @PostMapping("/proflie")
    public ResponseEntity<String> updateProfile(
            Authentication authentication,
            @RequestBody ProfileRequest request
    ){
        // 로그인된 사용자 이메일 가져오기
        String email = authentication.getName();
        log.info("프로필 업데이트 요청 계정: {}", email);

        // 프로필 수정 로직 실행
        service.updateProfile(email, request);
        return ResponseEntity.ok("프로필 성공적으로 업데이트 되었습니다");
    }
}