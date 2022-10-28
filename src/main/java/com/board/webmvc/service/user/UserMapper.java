package com.board.webmvc.service.user;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    UserVO login(String id);

    String selectUserList();

    void joinUser(UserVO user);

    void updateLatestLogin(int userIdx);
}
