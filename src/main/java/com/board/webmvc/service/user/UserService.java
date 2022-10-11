package com.board.webmvc.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserMapper userMapper;

    public UserVO login(UserVO param) {
        return userMapper.login(param.getUserId());
    }
}
