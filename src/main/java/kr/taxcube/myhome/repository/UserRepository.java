package kr.taxcube.myhome.repository;

import kr.taxcube.myhome.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
