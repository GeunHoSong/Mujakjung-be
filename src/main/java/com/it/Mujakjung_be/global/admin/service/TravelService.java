package com.it.Mujakjung_be.global.admin.service;

import com.it.Mujakjung_be.global.admin.dto.TravelDTO;
import com.it.Mujakjung_be.global.admin.entity.TravelEntity;
import com.it.Mujakjung_be.global.admin.repository.TravelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TravelService {

    private final TravelRepository repository;

    @Transactional
    // 1. 리턴 타입을 void에서 TravelDTO로 바꿔주자!
    public TravelDTO registerTravel(TravelDTO dto) {
        TravelEntity en = new TravelEntity();
        en.setCategory(dto.getCategory());
        en.setTitle(dto.getTitle());
        en.setLocation(dto.getLocation());
        en.setContent(dto.getContent());

        // 2. save()하면 DB가 만들어준 ID가 포함된 Entity가 나와.
        TravelEntity savedEntity = repository.save(en);

        // 3. 그 ID를 다시 DTO에 담아서 리액트한테 보내주는 거야.
        dto.setId(savedEntity.getId());
        return dto;
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public TravelDTO getTravelDetail(Long id) {
        return repository.findById(id).map(entity -> {
            TravelDTO dto = new TravelDTO();
            dto.setId(entity.getId()); // 상세 페이지 갈 때 ID는 필수니까 꼭 넣어주고!
            dto.setCategory(entity.getCategory());
            dto.setTitle(entity.getTitle());
            dto.setLocation(entity.getLocation());
            dto.setContent(entity.getContent());
            return dto;
        }).orElseThrow(() -> new RuntimeException("해당 여행지를 찾을 수 없습니다"));
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<TravelDTO> getAllTravels() {
        return repository.findAll().stream().map(en -> {
            TravelDTO dto = new TravelDTO();
            dto.setId(en.getId());
            dto.setTitle(en.getTitle());
            dto.setCategory(en.getCategory());
            dto.setLocation(en.getLocation());
            return dto;
        }).collect(Collectors.toList());
    }
}