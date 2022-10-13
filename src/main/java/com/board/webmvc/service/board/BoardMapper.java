package com.board.webmvc.service.board;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface BoardMapper {

    ArrayList<PostVO> postList(PostVO param);

    void postWrite(PostVO param);
}
