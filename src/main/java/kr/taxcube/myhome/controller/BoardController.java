package kr.taxcube.myhome.controller;

import kr.taxcube.myhome.model.Board;
import kr.taxcube.myhome.repository.BoardRepository;
import kr.taxcube.myhome.service.BoardService;
import kr.taxcube.myhome.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

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

    @GetMapping("/form")    // 하나만 조회
    public String form(Model model, @RequestParam(required = false) Long id) {
        if(id == null) {
            model.addAttribute("board", new Board());
        } else {
            Board board = boardRepository.findById(id).orElse(null);
            model.addAttribute("board", board);
        }

        return "board/form";
    }

    @PostMapping("/form")   // 쓰기
    public String postForm(@Valid Board board, BindingResult bindingResult, Authentication authentication) {

        boardValidator.validate(board, bindingResult);
        if (bindingResult.hasErrors()) {
            return "board/form";
        }
        // 보드안에 사용자 정보를 담아서 전달해줄 수 있으나
        // 사용자 ID를 직접 전달하면 POST 요청하는 곳에서 개발자 도구를
        // 열어서 분석해서 자신의 ID가 아니라 다른 사용자의 ID를 전달할
        // 수도 있다.
        // 본인이 작성하지 않은 글이 올라올 수 있다. 따라서
        // board.setUser(user); 부분을 사용하지 않고 서버에서 가지고
        // 있는 인증정보를 활용하여 POST할 때 서버쪽 인증 정보를 이용해서
        // 직접 넣어준다.
        // 인증 정보를 넣어주는 방법 중  authentication을
        // 사용하여 암호화 한 내용을 내부에서 전달하도록 한다.
        // Authentication authentication 파라미터를 선언해주면 여기에
        // 인증정보가 담아서 넘어온다. authentication.getName() 하면
        // user 테이블의 username을 담을 수 이다.

        // 다른 방법 파라미터로 가지고 올 수도 있지만 Service class나
        // 컨트롤러 외에 스프링에서 관리해주는 class에서 인증정보 가지고
        // 오고 싶을 때는 SecurityContextHolder라는 전역변수 이용해서
        // 가지고 올 수도 있다.
        // Authentication a = SecurityContextHolder.getContext().getAuthentication();

        // 컨트롤러에서 Pricipal pricial 을 이용해 가지고 올 수도 있다.
        // public String currentUserName(Principal pricipal) {
        //    return pricipal.getName();
        // }
        String username =  authentication.getName();
        boardService.save(username, board);
//        boardRepository.save(board);
        return "redirect:/board/list";
    }
}

