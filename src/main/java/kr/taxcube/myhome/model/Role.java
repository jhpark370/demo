package kr.taxcube.myhome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToMany(mappedBy = "roles")
    // Role을 줘야 할 때 POSTMAN 에서 볼 수 있듯이 재귀적 연결을 끊어준다.
    // Role을 가지고 있는 사용자들은 표시가 되지 않다.
    @JsonIgnore
    private List<User> users;

}
