package com.board.webmvc.usertest;

import com.board.webmvc.service.user.UserMapper;
import com.board.webmvc.service.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@Slf4j
@SpringBootTest
public class UserApplicationTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void contextLoads() {
        List<UserVO> list = userMapper.selectUserList();
        log.info("list = {}", list.get(0).getUserId());
    }
}
