package kr.taxcube.myhome.controller;

import kr.taxcube.myhome.model.Board;
import kr.taxcube.myhome.repository.BoardRepository;
import kr.taxcube.myhome.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardValidator boardValidator;


//    public String list(Model model) {
//        List<Board> boards = boardRepository.findAll();
//        model.addAttribute("boards",boards);
//        return"board/list";
//}
//    기본값을 2로해서 테스트해본다.
//    @GetMapping("/list")
//    public String list(Model model, @PageableDefault(size = 2) Pageable pageable, String searchText) {
////        한페이지에 20개씩 보여주겠다.
////        Page<Board> boards = boardRepository.findAll(PageRequest.of(0, 20));
////        전체 데이터 갯수
////        boards.getTotalElements();
//        Page<Board> boards = boardRepository.findAll(pageable);
////        시작페이지와 마지막 페이지 정의
//        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
//        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
////        현재페이지 비활성화를 위해 리스트 페이지에서 아래 코드 삽입
////        boards.getPageable().getPageNumber();
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//        model.addAttribute("boards", boards);
//        return "board/list";
//    }

    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 2) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchText) {
//        한페이지에 20개씩 보여주겠다.
//        Page<Board> boards = boardRepository.findAll(PageRequest.of(0, 20));
//        전체 데이터 갯수
//        boards.getTotalElements();
//        Page<Board> boards = boardRepository.findAll(pageable);
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
//        시작페이지와 마지막 페이지 정의
        int startPage = Math.max(1, boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
//        현재페이지 비활성화를 위해 리스트 페이지에서 아래 코드 삽입
//        boards.getPageable().getPageNumber();
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        return "board/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id) {
        if(id == null) {
            model.addAttribute("board", new Board());
        } else {
            Board board = boardRepository.findById(id).orElse(null);
            model.addAttribute("board", board);
        }

        return "board/form";
    }

    @PostMapping("/form")
    public String greetingSubmit(@Valid Board board, BindingResult bindingResult) {

        boardValidator.validate(board, bindingResult);

        if (bindingResult.hasErrors()) {
            return "board/form";
        }
        boardRepository.save(board);
        return "redirect:/board/list";
    }
}

