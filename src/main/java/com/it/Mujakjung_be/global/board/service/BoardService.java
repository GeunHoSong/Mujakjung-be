package com.it.Mujakjung_be.global.board.service;

import com.it.Mujakjung_be.global.board.dto.BoardDto;
import com.it.Mujakjung_be.global.board.entity.BoardEntity;
import com.it.Mujakjung_be.global.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository repository; // 주입!

    public BoardDto write(BoardDto dto) {
        BoardEntity board = BoardEntity.builder()
                .title(dto.getTitle())
                .writer(dto.getWriter())
                .content(dto.getContent())
                .build();

        return new BoardDto(repository.save(board));
    }

    public List<BoardDto> findAll(){
        return repository.findAll().stream().map(BoardDto::new).collect(Collectors.toList());
    }

    public void delete(Long id) {
    }
}
