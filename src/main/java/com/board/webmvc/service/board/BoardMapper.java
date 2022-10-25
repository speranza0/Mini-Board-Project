package com.board.webmvc.service.board;

import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;

@Mapper
public interface BoardMapper {

    BoardNumVO boardNum(int boardIdx);

    ArrayList<PostVO> postList(PostVO postVO);

    int getListCnt(PostVO postVO);

    PostVO postView(PostVO postVO);

    FileVO postView_attach(int postIdx);

    FileVO attachFileDown(FileVO fileVO);

    void postWrite(PostVO postVO);

    void postWrite_attach(FileVO fileVO);

    void postUpdate(PostVO postVO);

    int updateViewCnt(int postIdx);

    void deletePost(int postIdx);

    void deleteFile(String uuid);
}
