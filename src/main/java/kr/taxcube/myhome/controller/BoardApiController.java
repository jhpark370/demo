package kr.taxcube.myhome.controller;

import kr.taxcube.myhome.model.Board;
import kr.taxcube.myhome.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
class BoardApiController {

    @Autowired
    private BoardRepository repository;
    // Aggregate root

    @GetMapping("/boards")      // 조회 - 검색기능(제목 검색, 내용 검색)
    List<Board> all(@RequestParam(required = false, defaultValue = "") String title,
        @RequestParam(required = false, defaultValue = "") String content ) {
        // 타이틀만 조회 - sample
//        if(StringUtils.isEmpty(title)) {
//            return repository.findAll();
//        } else {
//            return repository.findByTitle(title);
//        }
        // 타이틀과 내용을 같이 조회
        if(StringUtils.isEmpty(title) && StringUtils.isEmpty(content)) {
            return repository.findAll();
        } else {
            return repository.findByTitleOrContent(title, content);
        }

    }
    // RESTFUL API 사용할 때 POSTMAN 사용하기 위해 다운로드
    @PostMapping("/boards")     // 쓰기
    Board newBoard(@RequestBody Board newBoard) {
        return repository.save(newBoard);
    }

    // Single item

    @GetMapping("/boards/{id}") // 하나만 조회
    Board one(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/boards/{id}") // 하나만 업데이트
    Board replaceBoard(@RequestBody Board newBoard, @PathVariable Long id) {

        return repository.findById(id)
                .map(board -> {
                    board.setTitle(newBoard.getTitle());
                    board.setContent(newBoard.getContent());
                    return repository.save(board);
                })
                .orElseGet(() -> {
                    newBoard.setId(id);
                    return repository.save(newBoard);
                });
    }

    @DeleteMapping("/boards/{id}")
    void deleteBoard(@PathVariable Long id) {
        repository.deleteById(id);
    }
}