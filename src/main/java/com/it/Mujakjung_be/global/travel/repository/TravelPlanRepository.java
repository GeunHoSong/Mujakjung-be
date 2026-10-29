package com.it.Mujakjung_be.global.travel.repository;

import com.it.Mujakjung_be.global.travel.entity.TravelPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelPlanRepository extends JpaRepository<TravelPlan , Long> {
    List<TravelPlan> findByMemberId(Long memberId);
}
