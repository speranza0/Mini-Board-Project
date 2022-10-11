package com.board.webmvc.service.user;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    UserVO login(String userId);

    List<UserVO> selectUserList();
}
