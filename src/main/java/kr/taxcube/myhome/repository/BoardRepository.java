package kr.taxcube.myhome.repository;

import kr.taxcube.myhome.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    List<Board> findByTitle(String title);
    List<Board> findByTitleOrContent(String title, String content);
// 메소드 규칙은 spring.io - project - spring data - (왼)spring jpa ->
// -> learn - 최신 Reference Docs - findby 로 검색하면 여러가지 예제가 나온다.
//    5.3.2. Query Creation 참조
//    4.5.1 XML configuration - Using filters (Example 61, 63, 64 )
//    List<Board> findByTitleAndContent(String title, String content);


}
