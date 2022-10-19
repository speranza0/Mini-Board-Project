package com.board.webmvc.service.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLSyntaxErrorException;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;

    public ArrayList<PostVO> postList(PostVO param) {
        return boardMapper.postList(param);
    }

    public int getListCnt(PostVO param) {
        return boardMapper.getListCnt(param);
    }

    public PostVO postView(PostVO param) {
        return boardMapper.postView(param);
    }

    public void postWrite(PostVO param) {
        boardMapper.postWrite(param);
    }

    public void postUpdate(PostVO param) {
        boardMapper.postUpdate(param);
    }

    public int updateViewCnt(int param) {
        return boardMapper.updateViewCnt(param);
    }

    public void deletePost(int param) {
        boardMapper.deletePost(param);
    }
}
