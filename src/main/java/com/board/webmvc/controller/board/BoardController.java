package com.board.webmvc.controller.board;

import com.board.webmvc.service.board.*;
import com.board.webmvc.service.user.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;


@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {
    private final BoardService boardService;

    private final FileStore fileStore;

    @GetMapping("/{boardName}/list")
    public String listView(BoardParam.Search searchBoardVO, @PathVariable String boardName, Model model) {

        BoardVO boardVO = boardService.getBoardByName(boardName);

        if(ObjectUtils.isEmpty(boardVO)) {
            return "error/error";
        }

        //페이징[s]
        Pagination pagination = new Pagination();
        pagination.setCurrentPageNo(searchBoardVO.getPageIndex());
        pagination.setRecordCountPerPage(searchBoardVO.getPageUnit());
        pagination.setPageSize(searchBoardVO.getPageSize());

        searchBoardVO.setFirstIndex(pagination.getFirstRecordIndex());
        searchBoardVO.setRecordCountPerPage(pagination.getRecordCountPerPage());

        int totCnt = boardService.getListCnt(boardVO.getIdx(), searchBoardVO);

        pagination.setTotalRecordCount(totCnt);

        searchBoardVO.setEndData(pagination.getLastPageNoOnPageList());
        searchBoardVO.setStartData(pagination.getFirstPageNoOnPageList());
        searchBoardVO.setPrev(pagination.getXprev());
        searchBoardVO.setNext(pagination.getXnext());

        model.addAttribute("boardVO", boardVO);
        model.addAttribute("searchVO", searchBoardVO);
        log.info("searchBoardVO {}",searchBoardVO);
        model.addAttribute("postList", boardService.postList(boardVO.getIdx(), searchBoardVO));
        model.addAttribute("totCnt",totCnt);
        model.addAttribute("totalPageCnt",(int)Math.ceil(totCnt / (double)searchBoardVO.getPageUnit()));
        model.addAttribute("pagination",pagination);
        //페이징[e]
        return "board/list";
    }

    @GetMapping("/{boardName}/detail/{postIdx}")
    public String detailView(BoardParam.Search searchBoardVO, @PathVariable String boardName, @PathVariable int postIdx, Model model) {
        BoardVO boardVO = boardService.getBoardByName(boardName);

        if(ObjectUtils.isEmpty(boardVO)) {
            return "error/error";
        }
        BoardParam.Post detailView = boardService.postView(postIdx);
        BoardParam.PreNext preView = boardService.postPreView(postIdx, detailView.getBoardIdx());
        BoardParam.PreNext nextView = boardService.postNextView(postIdx, detailView.getBoardIdx());

        if(detailView == null) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        if(preView != null) {
            model.addAttribute("preView", preView);
        }
        if(nextView != null) {
            model.addAttribute("nextView", nextView);
        }
        model.addAttribute("boardVO", boardVO);
        model.addAttribute("searchVO", searchBoardVO);
        model.addAttribute("detailView", detailView);
        return "board/detail";
    }

    @GetMapping("/{boardName}/edit")
    public String editView(@PathVariable String boardName, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        UserVO loginUser = (UserVO) session.getAttribute("loginUser");

        // 게시판이 없는 경우
        BoardVO boardVO = boardService.getBoardByName(boardName);
        if(ObjectUtils.isEmpty(boardVO)) {
            return "error/error";
        }

        // 일반 사용자가 관리자 게시판에 등록하려고 할때
        if("admin".equals(boardVO.getType()) && loginUser.getLevel() != 1) {
            return "error/error";
        }
        model.addAttribute("boardVO", boardVO);
        return "board/edit";
    }

    @PostMapping("/{boardName}/edit")
    public String edit(@Valid BoardParam.Create createBoardVO, @PathVariable String boardName, FileVO fileVO) throws ServletException, IOException {
        boardService.postWrite(createBoardVO);
        FileVO vo = fileStore.uploadFile(createBoardVO.getFile());
        if(vo != null) {
            fileVO.setPostIdx(createBoardVO.getIdx());
            fileVO.setBoardIdx(createBoardVO.getBoardIdx());
            fileVO.setOriginname(vo.getOriginname());
            fileVO.setPath(vo.getPath());
            fileVO.setType(vo.getType());
            fileVO.setSize(vo.getSize());
            fileVO.setUuid(vo.getUuid());
            boardService.postWrite_attach(fileVO);
        }
        return "redirect:/board/" + boardName + "/list";
    }

    @GetMapping("/attachFile")
    public ResponseEntity<Resource> attachFile(FileVO param) throws MalformedURLException {
        return fileStore.downloadAttach(param);
    }

    @GetMapping("/{boardName}/update/{postIdx}")
    public String updateView(@PathVariable String boardName, @PathVariable int postIdx, Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserVO loginUser = (UserVO) session.getAttribute("loginUser");

        // 게시판이 없는 경우
        BoardVO boardVO = boardService.getBoardByName(boardName);
        if(ObjectUtils.isEmpty(boardVO)) {
            return "error/error";
        }

        //게시글이 없는 경우
        BoardParam.Post detailView = boardService.postView(postIdx);
        if(detailView == null) {
            return "error/error";
        }

        // 일반 사용자가 관리자 게시글을 수정하려고 할때
        if("admin".equals(boardVO.getType()) && loginUser.getLevel() != 1) {
            return "error/error";
        }

        // 사용자가 다른 사용자 게시글을 수정하려고 할때
        if(loginUser.getLevel() != 1 && loginUser.getIdx() != detailView.getUserIdx()) {
            return "error/error";
        }

        model.addAttribute("boardVO", boardVO);
        model.addAttribute("detailView", detailView);
        return "board/update";
    }

    @PostMapping("/{boardName}/update/{postIdx}")
    public String update(@Valid BoardParam.Update updateBoardVO, @PathVariable String boardName, @PathVariable int postIdx, FileVO fileVO) throws ServletException, IOException {
        FileVO vo = fileStore.uploadFile(updateBoardVO.getFile());
        if(vo != null) {
            fileVO.setPostIdx(updateBoardVO.getIdx());
            fileVO.setBoardIdx(updateBoardVO.getBoardIdx());
            fileVO.setOriginname(vo.getOriginname());
            fileVO.setPath(vo.getPath());
            fileVO.setType(vo.getType());
            fileVO.setSize(vo.getSize());
            fileVO.setUuid(vo.getUuid());
            boardService.deleteFile(updateBoardVO.getUuid());
            boardService.postWrite_attach(fileVO);
        } else {
            if(updateBoardVO.getUuid() != null && "Y".equals(updateBoardVO.getFileDeleteYn())) {
                boardService.deleteFile(updateBoardVO.getUuid());
            }
        }
        boardService.postUpdate(updateBoardVO);
        return "redirect:/board/" + boardName + "/detail/" + postIdx;
    }

    @GetMapping("/{boardName}/delete/{postIdx}")
    public String delete(@PathVariable String boardName, @PathVariable int postIdx, HttpServletRequest request) {
        HttpSession session = request.getSession();
        UserVO loginUser = (UserVO) session.getAttribute("loginUser");

        // 게시판이 없는 경우
        BoardVO boardVO = boardService.getBoardByName(boardName);
        if(ObjectUtils.isEmpty(boardVO)) {
            return "error/error";
        }

        //게시글이 없는 경우
        BoardParam.Post detailView = boardService.postView(postIdx);
        if(detailView == null) {
            return "error/error";
        }

        // 일반 사용자가 관리자 게시글을 삭제하려고 할때
        if("admin".equals(boardVO.getType()) && loginUser.getLevel() != 1) {
            return "error/error";
        }

        // 사용자가 다른 사용자 게시글을 삭제하려고 할때
        if(loginUser.getLevel() != 1 && loginUser.getIdx() != detailView.getUserIdx()) {
            return "error/error";
        }

        boardService.deletePost(postIdx);
        return "redirect:/board/" + boardName + "/list";
    }
}
