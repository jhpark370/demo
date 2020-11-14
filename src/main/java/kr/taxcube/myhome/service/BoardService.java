package kr.taxcube.myhome.service;

import kr.taxcube.myhome.model.Board;
import kr.taxcube.myhome.model.User;
import kr.taxcube.myhome.repository.BoardRepository;
import kr.taxcube.myhome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    public Board save(String username, Board board) {
        User user = userRepository.findByUsername(username);
        board.setUser(user);
        return boardRepository.save(board);
    }

}
