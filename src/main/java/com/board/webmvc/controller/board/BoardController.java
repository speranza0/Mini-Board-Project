package com.board.webmvc.controller.board;

import com.board.webmvc.service.board.*;
import com.board.webmvc.service.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;


@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;
    private final FileStore fileStore;

    @Autowired
    public BoardController(BoardService boardService, FileStore fileStore) {
        this.boardService = boardService;
        this.fileStore = fileStore;
    }

    @GetMapping("/list")
    public String listView(@ModelAttribute("searchVO") PostVO postVO, Model model) {
        BoardNumVO boardNum = boardService.boardNumVO(postVO.getBoardIdx());

        if(boardNum == null || postVO.getBoardIdx() != boardNum.getIdx()) {
            return "error/error";
        }

        //페이징[s]
        Pagination pagination = new Pagination();
        pagination.setCurrentPageNo(postVO.getPageIndex());
        pagination.setRecordCountPerPage(postVO.getPageUnit());
        pagination.setPageSize(postVO.getPageSize());

        postVO.setFirstIndex(pagination.getFirstRecordIndex());
        postVO.setRecordCountPerPage(pagination.getRecordCountPerPage());

        int totCnt = boardService.getListCnt(postVO);

        pagination.setTotalRecordCount(totCnt);

        postVO.setEndData(pagination.getLastPageNoOnPageList());
        postVO.setStartData(pagination.getFirstPageNoOnPageList());
        postVO.setPrev(pagination.getXprev());
        postVO.setNext(pagination.getXnext());

        model.addAttribute("postList", boardService.postList(postVO));
        model.addAttribute("totCnt",totCnt);
        model.addAttribute("totalPageCnt",(int)Math.ceil(totCnt / (double)postVO.getPageUnit()));
        model.addAttribute("pagination",pagination);
        //페이징[e]
        return "board/list";
    }

    @GetMapping("/detail")
    public String detailView(@ModelAttribute("searchVO") PostVO postVO, Model model) {
        boardService.updateViewCnt(postVO.getIdx());
        PostVO detailView = boardService.postView(postVO);
        FileVO fileView = boardService.postView_attach(postVO.getIdx());

        if(detailView == null) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        model.addAttribute("detailView", detailView);
        model.addAttribute("fileView", fileView);
        return "board/detail";
    }

    @GetMapping("/edit")
    public String editView(@ModelAttribute("searchVO") PostVO postVO, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserVO loginUser = (UserVO) session.getAttribute("loginUser");

        // 게시판이 없는 경우
        BoardNumVO boardNum = boardService.boardNumVO(postVO.getBoardIdx());
        if(boardNum == null || postVO.getBoardIdx() != boardNum.getIdx()) {
            return "error/error";
        }

        // 일반 사용자가 관리자 게시판에 등록하려고 할때
        if("admin".equals(boardNum.getType()) && loginUser.getLevel() != 1) {
            return "error/error";
        }

        return "board/edit";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute("postVO") PostVO postVO, FileVO fileVO) throws ServletException, IOException {
        boardService.postWrite(postVO);
        FileVO vo = (FileVO) fileStore.uploadFile(postVO.getFile());
        if(vo != null) {
            fileVO.setPostIdx(postVO.getIdx());
            fileVO.setBoardIdx(postVO.getBoardIdx());
            fileVO.setOriginname(vo.getOriginname());
            fileVO.setPath(vo.getPath());
            fileVO.setType(vo.getType());
            fileVO.setSize(vo.getSize());
            fileVO.setUuid(vo.getUuid());
            boardService.postWrite_attach(fileVO);
        }
        return "redirect:/board/list?boardIdx=" + postVO.getBoardIdx();
    }

    @GetMapping("/attachFile")
    public ResponseEntity<Resource> attachFile(FileVO param) throws MalformedURLException {
        return fileStore.downloadAttach(param);
    }

    @GetMapping("/update")
    public String updateView(@ModelAttribute("searchVO") PostVO postVO, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserVO loginUser = (UserVO) session.getAttribute("loginUser");

        // 게시판이 없는 경우
        BoardNumVO boardNum = boardService.boardNumVO(postVO.getBoardIdx());
        if(boardNum == null || postVO.getBoardIdx() != boardNum.getIdx()) {
            return "error/error";
        }

        //게시글이 없는 경우
        PostVO detailView = boardService.postView(postVO);
        if(detailView == null) {
            return "error/error";
        }

        // 일반 사용자가 관리자 게시글을 수정하려고 할때
        if("admin".equals(boardNum.getType()) && loginUser.getLevel() != 1) {
            return "error/error";
        }

        // 사용자가 다른 사용자 게시글을 수정하려고 할때
        if(loginUser.getLevel() != 1 && loginUser.getIdx() != detailView.getUserIdx()) {
            return "error/error";
        }

        FileVO detailFile = boardService.postView_attach(postVO.getIdx());
        if(detailFile != null) {
            model.addAttribute("detailFile", detailFile);
        }
        model.addAttribute("detailView", detailView);
        return "board/update";
    }

    @PostMapping("/update")
    public String update(PostVO postVO, FileVO fileVO) throws ServletException, IOException {
        FileVO vo = (FileVO) fileStore.uploadFile(postVO.getFile());
        if(vo != null) {
            fileVO.setPostIdx(postVO.getIdx());
            fileVO.setBoardIdx(postVO.getBoardIdx());
            fileVO.setOriginname(vo.getOriginname());
            fileVO.setPath(vo.getPath());
            fileVO.setType(vo.getType());
            fileVO.setSize(vo.getSize());
            fileVO.setUuid(vo.getUuid());
            boardService.deleteFile(postVO.getUuid());
            boardService.postWrite_attach(fileVO);
        } else {
            if(postVO.getUuid() != null && "Y".equals(postVO.getFileDeleteYn())) {
                boardService.deleteFile(postVO.getUuid());
            }
        }
        boardService.postUpdate(postVO);
        return "redirect:/board/detail/?boardIdx=" + postVO.getBoardIdx() + "&idx=" + postVO.getIdx();
    }

    @GetMapping("/delete")
    public String delete(@ModelAttribute("searchVO") PostVO postVO, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserVO loginUser = (UserVO) session.getAttribute("loginUser");

        // 게시판이 없는 경우
        BoardNumVO boardNum = boardService.boardNumVO(postVO.getBoardIdx());
        if(boardNum == null || postVO.getBoardIdx() != boardNum.getIdx()) {
            return "error/error";
        }

        //게시글이 없는 경우
        PostVO detailView = boardService.postView(postVO);
        if(detailView == null) {
            return "error/error";
        }

        // 일반 사용자가 관리자 게시글을 삭제하려고 할때
        if("admin".equals(boardNum.getType()) && loginUser.getLevel() != 1) {
            return "error/error";
        }

        // 사용자가 다른 사용자 게시글을 삭제하려고 할때
        if(loginUser.getLevel() != 1 && loginUser.getIdx() != detailView.getUserIdx()) {
            return "error/error";
        }

        boardService.deletePost(postVO.getIdx());
        return "redirect:/board/list/?boardIdx=" + postVO.getBoardIdx();
    }
}
