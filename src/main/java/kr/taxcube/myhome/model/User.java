package kr.taxcube.myhome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private Boolean enabled;

    @ManyToMany
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private  List<Role> roles = new ArrayList<>();

    // 양방향 맵핑 - Board.java에서도 매핑되어 있다.
    // @ManyToOne
    // @JoinColumn(name = "user_id")
    // private User user;

    // CascadeType.ALL 사용자 삭제시 문제가 해결된다.
    // postman에서 DELETE localhost:8080/api/users/2 하면
    // 보드에서 해당 user 2 가 사용한 게시글 삭제되고, user 테이블의 user 2 도 삭제가 된다.

    // orphanRemoval = false 주면 --> postman에서 특정 id의 제목과 내용을 put해서
    // DB를 확인하면 정상적으로 수정된다.
    // orphanRemoval = true 주면 --> postman에서 특정 id의 제목과 내용을 put해서
    // DB를 확인하면 같은 user_id가 모두 삭제 되고 마지막 것만 남는다.
    // orphanRemoval 조금 위험하기도 하지만 어떤 때에는 아주 유용하게 사용될 수 있다.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Board> boards = new ArrayList<>();
}
