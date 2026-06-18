package com.it.Mujakjung_be.global.notice.service;

import com.it.Mujakjung_be.global.notice.dto.NoticeDto;
import com.it.Mujakjung_be.global.notice.entity.Notice;
import com.it.Mujakjung_be.global.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public NoticeDto findById(Long id) {
        Notice notice = repository.findById(id).orElseThrow(()-> new RuntimeException("해당 게시글이 없습니다"));
        return new NoticeDto(notice);
    }
    @Transactional
    public void updateNotice(Long id, NoticeDto dto) {
        // 1. 수정할 게시글이 있는지 먼저 확인
        Notice notice = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 공지 사항이 없습니다"+ id));
        // 2. 엔티티의 값을 DTO에서 받은 값으로 변경 (Dirty Checking 활용)
        // @Transactional이 붙어있기 때문에, 여기서 setter만 호출해도
        // 메서드가 끝날 때 자동으로 DB에 반영(Update 쿼리)됨
        notice.update(dto.getTitle(), dto.getContent());

    }
    @Transactional
    public void deteleById(Long id) {
        Notice notice = repository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 공지 사항이 없습니다" + id) );
        repository.delete(notice);
    }
}
