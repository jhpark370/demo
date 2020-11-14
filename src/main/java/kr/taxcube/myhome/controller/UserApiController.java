package kr.taxcube.myhome.controller;

import kr.taxcube.myhome.model.Board;
import kr.taxcube.myhome.model.User;
import kr.taxcube.myhome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
class UserApiController {

    @Autowired
    private UserRepository repository;
    // Aggregate root

    @GetMapping("/users")      // 조회 - BoardApiController에서 검색기능 제거
    List<User> all() {
        return repository.findAll();
    }
    // RESTFUL API 사용할 때 POSTMAN 사용하기 위해 다운로드
    @PostMapping("/users")     // 쓰기
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    // Single item

    @GetMapping("/users/{id}") // 하나만 조회
    User one(@PathVariable Long id) {
        return repository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}") // 하나만 업데이트
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
//                    user.setTitle(newUser.getTitle());
//                    user.setContent(newUser.getContent());
                    // 1. 기존에 있던 보드를 새로운 데이터(newUser.getBoards())로 바꾸고
                    // 주의) orphanRemoval = ture옵션은 부모가 없는 데이터는 모두 삭제 된다.
                        // 주의) User.java orphanRemoval = false 의 경우
                            // user.setBoards(newUser.getBoards());  이렇게 하고
                        // 주의) User.java orphanRemoval = true의 경우
                            // 이렇게 기존데이터 전부 삭제
                            // 지금 받은 데이터로 전부 바꿔준다.
                            // 결과는 같은 user_id 내용이 모두 삭제되고 현재의 보드 내용만 삽입된다.

                    user.getBoards().clear();
                    user.getBoards().addAll(newUser.getBoards());
                    for(Board board : user.getBoards()) {
                        // 2. 사용자 셋팅
                        board.setUser(user);
                    }
                    // 3. 저장한다.
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}