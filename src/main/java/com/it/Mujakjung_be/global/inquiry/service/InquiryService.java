package com.it.Mujakjung_be.global.inquiry.service;

import com.it.Mujakjung_be.global.inquiry.dto.InquiryRequestDto;
import com.it.Mujakjung_be.global.inquiry.entity.InquiryEntity;
import com.it.Mujakjung_be.global.inquiry.repository.InquiryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository repository;

    public void saveInquiry(InquiryRequestDto dto){
        InquiryEntity entity  = new InquiryEntity();
        entity.setEmail(dto.getEmail());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());

        repository.save(entity);
    }
}
