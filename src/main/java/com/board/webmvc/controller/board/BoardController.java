package com.board.webmvc.controller.board;

import com.board.webmvc.service.board.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.SQLSyntaxErrorException;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private FileStore fileStore;

    @GetMapping("/list")
    public String listView(@ModelAttribute("searchVO") PostVO postVO, Model model) {
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
    public String detailView(@ModelAttribute("searchVO") PostVO postVO, FileVO fileVO, Model model) {
        boardService.updateViewCnt(postVO.getIdx());
        PostVO detailView = boardService.postView(postVO);
        FileVO fileView = boardService.postView_attach(fileVO);
        if(detailView == null) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        model.addAttribute("detailView", detailView);
        model.addAttribute("fileView", fileView);
        return "board/detail";
    }

    @GetMapping("/edit")
    public String editView() {
        return "board/edit";
    }

    @PostMapping("/edit")
    public String edit(PostVO postVO, FileVO fileVO) throws ServletException, IOException {
        log.info("filevo={}", fileVO);
        FileVO vo = fileStore.uploadFile(fileVO.getUploadFile());
        boardService.postWrite(postVO);
        if(vo != null) {
            fileVO.setIdx(postVO.getIdx());
            fileVO.setBoardIdx(postVO.getBoardIdx());
            fileVO.setOriginname(vo.getOriginname());
            fileVO.setPath(vo.getPath());
            fileVO.setType(vo.getType());
            fileVO.setSize(vo.getSize());
            fileVO.setUuid(vo.getUuid());
            boardService.postWrite_attach(fileVO);
            log.info("post_idx={}", postVO.getIdx());
        }
        return "redirect:/board/list";
    }

    @GetMapping("/attachFile")
    public ResponseEntity<Resource> attachFile(FileVO param) throws MalformedURLException {
        return fileStore.downloadAttach(param);
    }

    @GetMapping("/update")
    public String updateView(@ModelAttribute("searchVO") PostVO postVO, Model model) {
        PostVO detailView = boardService.postView(postVO);
        model.addAttribute("detailView", detailView);
        return "board/update";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("searchVO") PostVO postVO) {
        boardService.postUpdate(postVO);
        return "redirect:/board/detail/?idx=" + postVO.getIdx();
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "redirect:/board/list";
    }

    @GetMapping("/delete")
    public String delete(@ModelAttribute("searchVO") PostVO postVO) {
        boardService.deletePost(postVO.getIdx());
        return "redirect:/board/list";
    }
}
