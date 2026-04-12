package com.it.Mujakjung_be.gobal.admin.repository;

import com.it.Mujakjung_be.gobal.admin.entity.TravelEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TravelRepository extends JpaRepository<TravelEntity , Long> {
    Optional<TravelEntity> findByTitle(String title);
}
