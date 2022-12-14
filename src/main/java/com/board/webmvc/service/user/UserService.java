package com.board.webmvc.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UserVO login(String id, String password) {
        UserVO user = userMapper.login(id);
        if(user == null) {
            throw new RuntimeException("아이디가 존재하지 않습니다.");
        }

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("비밀번호가 맞지 않습니다.");
        }
        return user;
    }

    @Transactional
    public void joinUser(UserVO user) {
        String password = passwordEncoder.encode(user.getPassword());
        user.setPassword(password);
        userMapper.joinUser(user);
    }

    public void updateLatestLogin(int userIdx) {
        userMapper.updateLatestLogin(userIdx);
    }
}
