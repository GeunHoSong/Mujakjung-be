package com.it.Mujakjung_be.gobal.memeber.controller;

import com.it.Mujakjung_be.gobal.memeber.dto.JoinRequest;
import com.it.Mujakjung_be.gobal.memeber.dto.LoginRequest;
import com.it.Mujakjung_be.gobal.memeber.dto.LoginResponse;
import com.it.Mujakjung_be.gobal.memeber.repository.MemberRepository;
import com.it.Mujakjung_be.gobal.memeber.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){

        LoginResponse response = service.login(request);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public String test(){
        return "테스트 성공";
    }




}
