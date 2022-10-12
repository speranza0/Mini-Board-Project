package com.board.webmvc.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;

    public UserVO login(String id) {
        return userMapper.login(id);
    }

    @Transactional
    public void joinUser(UserVO user) {
        userMapper.joinUser(user);
    }
}
