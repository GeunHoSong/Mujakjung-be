package com.it.Mujakjung_be.global.notice.service;

import com.it.Mujakjung_be.global.notice.dto.NoticeDto;
import com.it.Mujakjung_be.global.notice.entity.Notice;
import com.it.Mujakjung_be.global.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository repository;

    public void saveNotice(NoticeDto dto){
        Notice notice = Notice.builder().title(dto.getTitle()).writer(dto.getWriter()).content(dto.getContent()).build();
        repository.save(notice);
    }

    public List<NoticeDto> findAll(){
        return repository.findAll().stream().map(NoticeDto::new).collect(Collectors.toList());
    }
}
