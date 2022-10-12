package com.board.webmvc.service.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface UserMapper {

    UserVO login(String id);

    String selectUserList();

    void joinUser(UserVO user);
}
