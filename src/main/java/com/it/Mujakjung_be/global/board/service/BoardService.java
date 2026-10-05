package com.it.Mujakjung_be.global.board.service;

import com.it.Mujakjung_be.global.board.dto.BoardDto;
import com.it.Mujakjung_be.global.board.entity.BoardEntity;
import com.it.Mujakjung_be.global.board.repository.BoardRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository repository; // 주입!

    public BoardDto write(BoardDto dto) throws IOException {
        String fileName  = null ;
        String filePath = null;

        if(dto.getFile() != null && !dto.getFile().isEmpty()){
            String uploadDir = "C:/uploads/";
            File uploadFolder = new File(uploadDir);
            if(!uploadFolder.exists()) uploadFolder.mkdirs();

            fileName = UUID.randomUUID().toString() + "_" + dto.getFile().getOriginalFilename();
            filePath = uploadDir  + fileName;
            dto.getFile().transferTo(new File(filePath));
        }
        BoardEntity board = BoardEntity.builder()
                .title(dto.getTitle())
                .writer(dto.getWriter())
                .content(dto.getContent())
                .fileName(fileName).filePath(filePath)
                .build();

        // 수정 전: return new BoardDto(repository.save(board));
        // 수정 후:
        BoardEntity savedBoard = repository.save(board);
        return BoardDto.fromEntity(savedBoard); // 여기서 BoardDto 내부에 만든 static 메서드를 호출!
    }

    public List<BoardDto> findAll() {
        return repository.findAll().stream()
                .map(BoardDto::fromEntity) // 👈 이렇게 수정하세요!
                .collect(Collectors.toList());
    }

    public void delete(Long id) {
       repository.deleteById(id);
    }

    public BoardDto findById(Long id) {
        BoardEntity board = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("게시글 없음"));

        return BoardDto.builder().id(board.getId()).title(board.getTitle()).writer(board.getWriter()).content(board.getContent()).build();
    }

    @Transactional // 데이터 변경이 일어날 때는 꼭 붙여줘!
    public Long update(Long id, BoardDto dto) {
        BoardEntity board = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 없습니다. id=" + id));

        // 엔티티 내부에 update 메서드를 만들어두는 것이 객체지향적이야
        board.update(dto.getTitle(), dto.getContent());

        return id;
    }
}
