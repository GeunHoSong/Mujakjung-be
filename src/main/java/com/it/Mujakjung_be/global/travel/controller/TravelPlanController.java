package com.it.Mujakjung_be.global.travel.controller;

import com.it.Mujakjung_be.global.member.entity.MemberEntity;
import com.it.Mujakjung_be.global.member.repository.MemberRepository;
import com.it.Mujakjung_be.global.travel.entity.TravelPlan;
import com.it.Mujakjung_be.global.travel.repository.TravelPlanRepository;
import com.it.Mujakjung_be.global.travel.service.TravelPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mypage/travel-plans")
public class TravelPlanController {
    private final TravelPlanService service;
    private final TravelPlanRepository repository;
    private final MemberRepository mr;
    // 여행 일정 저장
    @PostMapping
    public ResponseEntity<?> saveTravelPlan(@RequestBody Map<String , String> request, Authentication au){
        // 로그인 회원 확인
        if(au == null || !au.isAuthenticated()){
            return ResponseEntity.status(401).body("로그인이 필요 합니다");
        }
        // 여행 제목 받기
        String title = request.get("title");
        if (title == null || title.isBlank()){
            return ResponseEntity.badRequest().body("여행 제목을 입력을 하세요");
        }
        // 로그인 한 회원 조회
        MemberEntity member = mr.findByEmail(au.getName()).orElseThrow(()-> new RuntimeException("회원을 찾을 수 없습니다"));

        // 여행 일정 생성
        TravelPlan travelPlan = new TravelPlan();
        travelPlan.setMemberId(member.getId());
        travelPlan.setTitle(title);

        TravelPlan saved = service.saveTravelPlan(title, au.getName());

        return ResponseEntity.ok(Map.of("message", "여행 일정 저장 성공", "id",saved.getId(), "title", saved.getTitle()));

    }
    @GetMapping("/list")
    public ResponseEntity<?> getTravelList(Authentication au ){
        if(au == null || !au.isAuthenticated()){
            return ResponseEntity.status(401).body("로그인이 필요 합니다");

        }
        MemberEntity member  = mr.findByEmail(au.getName()).orElseThrow(()-> new RuntimeException("회원을 찾을 수 없습니다"));
        List<TravelPlan> travelPlans= service.getTravelList(au.getName());
        List<Map<String , Object>> result = travelPlans.stream().map(plan -> Map.<String , Object>of("id", plan.getId(), "title", plan.getTitle())).toList();
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTravelPlan (@PathVariable Long id , Authentication au){
        if(au == null || !au.isAuthenticated()){
            return ResponseEntity.status(401).body("로그인이 필요 합니다");
        }
        MemberEntity member = mr.findByEmail(au.getName()).orElseThrow(()-> new RuntimeException("여행지를 찾을 수 가 없습니다"));
        TravelPlan travelPlan  = repository.findById(id).orElse(null);

        if(travelPlan == null){
            return ResponseEntity.notFound().build();
        }
        // 로그인 회원 본인의 일정인지 확인
        if(!travelPlan.getMemberId().equals(member.getId())){
            return ResponseEntity.status(403).body("삭제 권한이 없습니다");
        }
        service.deleteTravelPlan(id , au.getName());
        return ResponseEntity.ok(Map.of("message", "여행 일정 삭제 완료"));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateTravelPlans(@PathVariable Long id , @RequestBody Map<String , String> request, Authentication au ){
        // 로그인 여부확인
        if(au == null || !au.isAuthenticated()){
            return ResponseEntity.status(401).body("로그인이 필요 합니다");
        }

         // 수정 할 제목 확인
        String title= request.get("title");

        if(title == null || title.isBlank()){
            return ResponseEntity.badRequest().body("여행 제목을 입력 하세요");
        }

        // 로그인 한 회원 조회
        MemberEntity member = mr.findByEmail(au.getName()).orElseThrow(()-> new RuntimeException("회원을 찾을 수가 없습니다"));
        // 수정할 여행 일정 조회
        TravelPlan travelPlan = repository.findById(id).orElse(null);
        if(travelPlan.getMemberId().equals(member.getId())){
            return ResponseEntity.status(403).body("수정 권환이 없습니다");
        }
        // 제목 수정
        travelPlan.setTitle(title);

        // DB 에 저장
        TravelPlan updateTravel = service.updateTravelPlan(id, title , au.getName());
        // 수정 결과 반환
        return ResponseEntity.ok(Map.of("message", "여행 일정 수정 완료", "id", updateTravel.getId(), "title", updateTravel.getTitle()));


    }
}
