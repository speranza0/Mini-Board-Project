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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/{boardName}/list")
    public String listView(BoardParam.Search searchBoardVO, @PathVariable String boardName, Model model) {
        //게시판이 없는 경우
        BoardVO boardVO = boardService.getBoardByName(boardName);
        if(ObjectUtils.isEmpty(boardVO)) {
            return "error/error";
        }

        //페이징[s]
        int totCnt = boardService.getListCnt(boardVO.getIdx(), searchBoardVO);
        Pagination pagination = new Pagination();
        pagination.setTotalRecordCount(totCnt);
        pagination.setCurrentPageNo(searchBoardVO.getPageIndex());
        pagination.setRecordCountPerPage(searchBoardVO.getPageUnit());
        pagination.setPageSize(searchBoardVO.getPageSize());

        searchBoardVO.setFirstIndex(pagination.getFirstRecordIndex());
        searchBoardVO.setRecordCountPerPage(pagination.getRecordCountPerPage());
        searchBoardVO.setEndData(pagination.getLastPageNoOnPageList());
        searchBoardVO.setStartData(pagination.getFirstPageNoOnPageList());
        searchBoardVO.setPrev(pagination.getXprev());
        searchBoardVO.setNext(pagination.getXnext());

        model.addAttribute("boardVO", boardVO);
        model.addAttribute("searchVO", searchBoardVO);
        model.addAttribute("postList", boardService.postList(boardVO.getIdx(), searchBoardVO));
        model.addAttribute("totCnt",totCnt);
        model.addAttribute("totalPageCnt",(int)Math.ceil(totCnt / (double)searchBoardVO.getPageUnit()));
        model.addAttribute("pagination",pagination);
        //페이징[e]
        return "board/list";
    }

    @GetMapping("/{boardName}/detail/{postIdx}")
    public String detailView(BoardParam.Search searchBoardVO, @PathVariable String boardName, @PathVariable int postIdx, Model model) {
        // 게시판이 없는 경우
        BoardVO boardVO = boardService.getBoardByName(boardName);
        if(ObjectUtils.isEmpty(boardVO)) {
            return "error/error";
        }

        BoardParam.Post detailView = boardService.postView(postIdx);
        BoardParam.PreNext preView = boardService.postPreView(postIdx, detailView.getBoardIdx());
        BoardParam.PreNext nextView = boardService.postNextView(postIdx, detailView.getBoardIdx());

        if(ObjectUtils.isEmpty(detailView)) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        if(!ObjectUtils.isEmpty(preView)) {
            model.addAttribute("preView", preView);
        }
        if(!ObjectUtils.isEmpty(nextView)) {
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
    public String edit(@Valid BoardParam.Create createBoardVO, @PathVariable String boardName, RedirectAttributes redirectAttributes) throws ServletException, IOException {
        boardService.postWrite(createBoardVO);
        redirectAttributes.addAttribute("boardName", boardName);
        return "redirect:/board/{boardName}/list";
    }

    @GetMapping("/attachFile")
    public ResponseEntity<Resource> attachFile(FileVO param) throws MalformedURLException {
        return boardService.attachFileDown(param);
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
        if(ObjectUtils.isEmpty(detailView)) {
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
    public String update(@Valid BoardParam.Update updateBoardVO, @PathVariable String boardName, @PathVariable int postIdx, RedirectAttributes redirectAttributes) throws ServletException, IOException {
        boardService.postUpdate(updateBoardVO);
        redirectAttributes.addAttribute("boardName", boardName);
        redirectAttributes.addAttribute("postIdx", postIdx);
        return "redirect:/board/{boardName}/detail/{postIdx}";
    }

    @GetMapping("/{boardName}/delete/{postIdx}")
    public String delete(@PathVariable String boardName, @PathVariable int postIdx, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        UserVO loginUser = (UserVO) session.getAttribute("loginUser");

        // 게시판이 없는 경우
        BoardVO boardVO = boardService.getBoardByName(boardName);
        if(ObjectUtils.isEmpty(boardVO)) {
            return "error/error";
        }

        //게시글이 없는 경우
        BoardParam.Post detailView = boardService.postView(postIdx);
        if(ObjectUtils.isEmpty(detailView)) {
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
        redirectAttributes.addAttribute("boardName", boardName);
        return "redirect:/board/{boardName}/list";
    }
}
