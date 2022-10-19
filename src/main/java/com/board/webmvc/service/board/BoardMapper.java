package com.board.webmvc.service.board;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface BoardMapper {

    ArrayList<PostVO> postList(PostVO postVO);

    int getListCnt(PostVO postVO);

    PostVO postView(PostVO postVO);

    FileVO postView_attach(FileVO fileVO);

    FileVO attachFileDown(FileVO fileVO);

    void postWrite(PostVO postVO);

    void postWrite_attach(FileVO fileVO);

    void postUpdate(PostVO postVO);

    int updateViewCnt(int postIdx);

    void deletePost(int postIdx);
}
