package com.board.webmvc.service.board;

import com.board.webmvc.controller.board.BoardParam;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;

    public BoardVO getBoardByName(String boardName) {
        return boardMapper.getBoardByName(boardName);
    }

    public ArrayList<PostVO> postList(int boardIdx, BoardParam.Search searchBoardVO) {
        return boardMapper.postList(boardIdx, searchBoardVO);
    }

    public int getListCnt(int boardIdx, BoardParam.Search searchBoardVO) {
        return boardMapper.getListCnt(boardIdx, searchBoardVO);
    }

    public PostVO postView(int param) {
        return boardMapper.postView(param);
    }

    public FileVO postView_attach(int postIdx) {
        return boardMapper.postView_attach(postIdx);
    }

    public FileVO attachFileDown(FileVO param) {
        return boardMapper.attachFileDown(param);
    }

    public void postWrite(BoardParam.Create createBoardVO) {
        boardMapper.postWrite(createBoardVO);
    }

    public void postWrite_attach(FileVO param) {
        boardMapper.postWrite_attach(param);
    }

    public void postUpdate(BoardParam.Update updateBoardVO) {
        boardMapper.postUpdate(updateBoardVO);
    }

    public int updateViewCnt(int param) {
        return boardMapper.updateViewCnt(param);
    }

    public void deletePost(int param) {
        boardMapper.deletePost(param);
    }

    public void deleteFile(String param) {
        boardMapper.deleteFile(param);
    }
}
