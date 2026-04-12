package com.it.Mujakjung_be.gobal.admin.service;

import com.it.Mujakjung_be.gobal.admin.dto.TravelDTO;
import com.it.Mujakjung_be.gobal.admin.entity.TravelEntity;
import com.it.Mujakjung_be.gobal.admin.repository.TravelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository repository;


    @Transactional
    public void registerTravel(TravelDTO dto){
        //  DTO를 Entity로 변환 (엔티티에 생성자나 Builder가 있어야 해)
        TravelEntity en = new TravelEntity();
        en.setCategory(dto.getCategory());
        en.setTitle(dto.getTitle());
        en.setLocation(dto.getLocation());
        en.setContent(dto.getContent());

        repository.save(en);
    }
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public TravelDTO getTravelDetail(Long id){
        return repository.findById(id).map(entity -> {
            TravelDTO dto  = new TravelDTO();
            dto.setCategory(entity.getCategory());
            dto.setTitle(entity.getTitle());
            dto.setLocation(entity.getLocation());
            dto.setContent(entity.getContent());
            return dto;
        }).orElseThrow(()-> new  RuntimeException("해당 여행지를 찾을 수 없습니다"));
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<TravelDTO> getAllTravels(){
        return repository.findAll().stream().map(en ->{
            TravelDTO dto = new TravelDTO();
            dto.setId(en.getId());
            dto.setTitle(en.getTitle());
            dto.setCategory(en.getCategory());
            dto.setLocation(en.getLocation());
            return dto;
        }).collect(Collectors.toList());
    }







}
