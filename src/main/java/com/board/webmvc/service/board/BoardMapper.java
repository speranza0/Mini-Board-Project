package com.board.webmvc.service.board;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface BoardMapper {

    ArrayList<PostVO> postList(PostVO postVO);

    int getListCnt(PostVO postVO);

    PostVO postView(PostVO postVO);

    void postWrite(PostVO postVO);

    void postUpdate(PostVO postVO);

    int updateViewCnt(int postIdx);

    void deletePost(int postIdx);
}
