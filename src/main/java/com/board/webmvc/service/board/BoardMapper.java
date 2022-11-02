package com.board.webmvc.service.board;

import com.board.webmvc.controller.board.BoardParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;

@Mapper
public interface BoardMapper {

    BoardVO getBoardByName(String boardName);

    ArrayList<PostVO> postList(@Param("boardIdx") int boardIdx, @Param("searchBoardVO") BoardParam.Search searchBoardVO);

    int getListCnt(@Param("boardIdx") int boardIdx, @Param("searchBoardVO") BoardParam.Search searchBoardVO);

    BoardParam.Post postView(int postIdx);

    ArrayList<BoardParam.PreNext> postPreNext(@Param("idx") int postIdx, @Param("boardIdx") int boardIdx);

    BoardParam.Post postView_attach(int postIdx);

    FileVO attachFileDown(FileVO fileVO);

    void postWrite(@Param("createBoardVO") BoardParam.Create createBoardVO);

    void postWrite_attach(FileVO fileVO);

    void postUpdate(@Param("updateBoardVO") BoardParam.Update updateBoardVO);

    int updateViewCnt(int postIdx);

    void deletePost(int postIdx);

    void deleteFile(String uuid);
}
