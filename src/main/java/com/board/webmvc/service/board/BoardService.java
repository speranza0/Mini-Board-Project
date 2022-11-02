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

    public BoardParam.Post postView(int param) {
        BoardParam.Post vo = new BoardParam.Post();
        BoardParam.Post fileVo = boardMapper.postView_attach(param);
        BoardParam.Post postVo = boardMapper.postView(param);
        boardMapper.updateViewCnt(param);

        BoardParam.Post.PostBuilder postBuilder = vo.builder();

        postBuilder.idx(postVo.getIdx())
                .boardIdx(postVo.getBoardIdx())
                .userIdx(postVo.getUserIdx())
                .title(postVo.getTitle())
                .content(postVo.getContent())
                .regdate(postVo.getRegdate())
                .hit(postVo.getHit())
                .id(postVo.getId())
                .nickname(postVo.getNickname());

        if(fileVo != null) {
            postBuilder.uuid(fileVo.getUuid())
                    .originname(fileVo.getOriginname())
                    .size(fileVo.getSize())
                    .type(fileVo.getType())
                    .path(fileVo.getPath())
                    .build();
        }

        return postBuilder.build();
    }

    public BoardParam.PreNext postPreView(int postIdx, int boardIdx) {
        ArrayList<BoardParam.PreNext> prev = boardMapper.postPreNext(postIdx, boardIdx);
        return prev.stream().filter(m -> m.getPostType().equals("prev")).findFirst().orElse(null);
    }

    public BoardParam.PreNext postNextView(int postIdx, int boardIdx) {
        ArrayList<BoardParam.PreNext> next = boardMapper.postPreNext(postIdx, boardIdx);
        return next.stream().filter(m -> m.getPostType().equals("next")).findFirst().orElse(null);
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
