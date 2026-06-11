import com.it.Mujakjung_be.global.board.dto.BoardDto;
import com.it.Mujakjung_be.global.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {
    private final BoardService service;

    @PostMapping("/save")
    // @RequestParam 대신 @ModelAttribute를 사용해야 formData를 객체로 받을 수 있어!
    public ResponseEntity<BoardDto> save(@ModelAttribute BoardDto dto){
        return ResponseEntity.ok(service.write(dto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<BoardDto>> list(){
        return ResponseEntity.ok(service.findAll());
    }
    @DeleteMapping("/delete/id")
    public ResponseEntity<BoardDto> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
