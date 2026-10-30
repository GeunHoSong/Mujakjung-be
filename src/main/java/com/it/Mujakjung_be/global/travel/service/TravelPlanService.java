package com.it.Mujakjung_be.global.travel.service;

import com.it.Mujakjung_be.global.member.entity.MemberEntity;
import com.it.Mujakjung_be.global.member.repository.MemberRepository;
import com.it.Mujakjung_be.global.travel.entity.TravelPlan;
import com.it.Mujakjung_be.global.travel.repository.TravelPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TravelPlanService {

    private final TravelPlanRepository repository;
    private final MemberRepository mr;
    @Transactional
    public TravelPlan saveTravelPlan(String title, String email) {
        // 로그인 회원 조회
        MemberEntity member = mr.findByEmail(email).orElseThrow(()-> new RuntimeException("회원을 조회 할 수 없습니다"));
        // 여행 일정 조회
        TravelPlan travelPlan  = new TravelPlan();
        travelPlan.setMemberId(member.getId());
        travelPlan.setTitle(title);
        return repository.save(travelPlan);
    }
    public List<TravelPlan> getTravelList(String name) {
        // 로그인 회원 조회
        MemberEntity member = mr.findByEmail(name).orElseThrow(()-> new RuntimeException("회원을 찾을수 없습니다"));
        // 해당 회원의 여행 일정 조회
        return repository.findByMemberId(member.getId());
    }
    @Transactional
    public void deleteTravelPlan(Long id, String name) {
        // 로그인 회원 조회
         MemberEntity member = mr.findByEmail(name).orElseThrow(()-> new RuntimeException("회원을 찾을 수 없습니다"));
         // 삭제 일정 조회
        TravelPlan travelPlan = repository.findById(id).orElseThrow(()-> new RuntimeException("여행 일정을 찾을 수 없습니다"));
        // 본인 일정인지 확인
        if(!travelPlan.getMemberId().equals(member.getId())){
            throw  new RuntimeException("삭제 권한이 없습니다 ");
        }
        // 여행 일정 삭제
        repository.delete(travelPlan);

    }
    // 여행 일정 제목 수정
    @Transactional
    public TravelPlan updateTravelPlan(Long id , String title, String email){
        // 로그인 회원 조회
        MemberEntity member = mr.findByEmail(email).orElseThrow(()-> new RuntimeException("회원을 찾을 수 없습니다"));
        // 수정할 일정 조회
        TravelPlan travelPlan = repository.findById(id).orElseThrow(()-> new RuntimeException("여행 일정을 찾을 수 없습니다") );
        // 본인인지확인
        if(!travelPlan.getMemberId().equals(member.getId())){
            throw  new RuntimeException("수정 권환이 없습니다");
        }
        // 제목 수정
        travelPlan.setTitle(title);
        return  travelPlan;
    }

}
