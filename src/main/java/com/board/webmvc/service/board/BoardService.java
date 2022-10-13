package com.board.webmvc.service.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;

    public ArrayList<PostVO> postList(PostVO param) {
        return boardMapper.postList(param);
    }

    public void postWrite(PostVO param) {
        boardMapper.postWrite(param);
    }
}
