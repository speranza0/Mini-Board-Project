package com.board.webmvc.service.board;

import com.board.webmvc.controller.board.BoardParam;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardMapper boardMapper;

    private final FileStore fileStore;

    // 게시판 정보 조회
    public BoardVO getBoardByName(String boardName) {
        return boardMapper.getBoardByName(boardName);
    }

    // 게시판 게시글 리스트 조회
    public List<PostVO> postList(int boardIdx, BoardParam.Search searchBoardVO) {
        return boardMapper.postList(boardIdx, searchBoardVO);
    }

    // 게시판 게시글 리스트 조회된 갯수
    public int getListCnt(int boardIdx, BoardParam.Search searchBoardVO) {
        return boardMapper.getListCnt(boardIdx, searchBoardVO);
    }

    // 게시글 상세 페이지 조회
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

    // 상세 페이지 이전글 보여주기
    public BoardParam.PreNext postPreView(int postIdx, int boardIdx) {
        List<BoardParam.PreNext> prev = boardMapper.postPreNext(postIdx, boardIdx);
        return prev.stream().filter(m -> m.getPostType().equals("prev")).findFirst().orElse(null);
    }

    //상세 페이지 다음글 보여주기
    public BoardParam.PreNext postNextView(int postIdx, int boardIdx) {
        List<BoardParam.PreNext> next = boardMapper.postPreNext(postIdx, boardIdx);
        return next.stream().filter(m -> m.getPostType().equals("next")).findFirst().orElse(null);
    }

    // 첨부파일 다운로드
    public ResponseEntity<Resource> attachFileDown(FileVO param) throws MalformedURLException {
        return fileStore.downloadAttach(param);
    }

    // 글쓰기 등록
    @Transactional
    public void postWrite(BoardParam.Create createBoardVO) throws ServletException, IOException {
        boardMapper.postWrite(createBoardVO);
        FileVO vo = fileStore.uploadFile(createBoardVO.getFile());
        if(vo != null) {
            vo.setPostIdx(createBoardVO.getIdx());
            vo.setBoardIdx(createBoardVO.getBoardIdx());
            boardMapper.postWrite_attach(vo);
        }
    }

    // 글 수정 등록
    @Transactional
    public void postUpdate(BoardParam.Update updateBoardVO) throws ServletException, IOException {
        FileVO vo = fileStore.uploadFile(updateBoardVO.getFile());
        if(vo != null) {
            vo.setPostIdx(updateBoardVO.getIdx());
            vo.setBoardIdx(updateBoardVO.getBoardIdx());
            boardMapper.deleteFile(updateBoardVO.getUuid());
            boardMapper.postWrite_attach(vo);
        } else {
            if(updateBoardVO.getUuid() != null && "Y".equals(updateBoardVO.getFileDeleteYn())) {
                boardMapper.deleteFile(updateBoardVO.getUuid());
            }
        }
        boardMapper.postUpdate(updateBoardVO);
    }

    // 게시글 삭제
    public void deletePost(int param) {
        boardMapper.deletePost(param);
    }

}
