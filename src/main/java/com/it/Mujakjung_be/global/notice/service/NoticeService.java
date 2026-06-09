package com.it.Mujakjung_be.global.notice.service;

import com.it.Mujakjung_be.global.notice.dto.NoticeDto;
import com.it.Mujakjung_be.global.notice.entity.Notice;
import com.it.Mujakjung_be.global.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository repository;

    public void saveNotice(NoticeDto dto){
        Notice notice = Notice.builder().title(dto.getTitle()).writer(dto.getWriter()).content(dto.getContent()).build();
        repository.save(notice);
    }
}
