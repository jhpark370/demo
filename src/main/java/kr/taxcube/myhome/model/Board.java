package kr.taxcube.myhome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min=2, max=30, message = "제목은 2자이상 30자 이하입니다.")
    private String title;
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id")
    // 보드에서도 사용자들이 있기 때문에 여기에도 @JsonIgnore 걸여준다.
    // Role을 줘야 할 때 POSTMAN 에서 볼 수 있듯이 재귀적 연결을 끊어준다.
    // Role을 가지고 있는 사용자들은 표시가 되지 않다.
    // POSTMAN 에서 localhost:8080/api/users GET하면 users에서
    // 회원가입하고 role에서 권한을 얻은 사용자, boards에서 그 사용자가
    // 게시한 내용을 보여준다.
    @JsonIgnore
    private User user;

}
