package com.it.Mujakjung_be.gobal.memeber.controller;

import com.it.Mujakjung_be.gobal.memeber.dto.JoinRequest;
import com.it.Mujakjung_be.gobal.memeber.repository.MemberRepository;
import com.it.Mujakjung_be.gobal.memeber.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {

    private final MemberService service;

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody JoinRequest request){
        try {
            service.save(request);
            return ResponseEntity.ok("ok");
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage()); // 400
        }



    }







}
