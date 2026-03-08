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
@CrossOrigin(origins = "http://lcalhost:5173")
public class MemberController {

    private final MemberService service;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinRequest request){
        service.save(request);
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
