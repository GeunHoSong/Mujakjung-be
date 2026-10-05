package com.it.Mujakjung_be.global.notice.service;
import com.it.Mujakjung_be.global.notice.dto.NoticeDto;
import com.it.Mujakjung_be.global.notice.entity.Notice;
import com.it.Mujakjung_be.global.notice.repository.NoticeRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) //
public class NoticeService {
    private final NoticeRepository repository;

    // 글 등록
    @Transactional // 수정이 필요하므로 readOnly=false (기본값)
    public void saveNotice(NoticeDto dto){
        Notice notice = Notice.builder()
                .title(dto.getTitle())
                .writer(dto.getWriter())
                .content(dto.getContent())
                .build();
        repository.save(notice);
    }

    // 글 목록 
    public List<NoticeDto> findAll(){
        return repository.findAll().stream().map(NoticeDto::new).collect(Collectors.toList());
    }

    // 상세 페이지 화면 
    public NoticeDto findById(Long id){
        Notice notice = repository.findById(id).orElseThrow(()-> new RuntimeException("해당 공지 사항이 없습니다"));
        return new NoticeDto(notice);
    }

    // 업데이트 
    @Transactional // 수정이 필요하므로 readOnly=false
    public void updateNotice(Long id, NoticeDto dto){ // 👈 dto를 꼭 받아야 해!
        Notice notice = repository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 공지 사항이 없습니다"));
        // 여기서 엔티티의 값을 변경해주면 트랜잭션 끝날 때 자동으로 DB에 저장됨
        notice.update(dto.getTitle(), dto.getContent());
    }

    // 삭제 
    @Transactional // 삭제가 필요하므로 readOnly=false
    public void deleteById(Long id){
        Notice notice = repository.findById(id).orElseThrow(()-> new IllegalArgumentException("해당 공지 사항이 없습니다"));
        repository.delete(notice);
    }
}