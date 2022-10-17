package com.board.webmvc.controller.board;

import com.board.webmvc.service.board.BoardService;
import com.board.webmvc.service.board.PostVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public String listView(PostVO postVO, Model model) {
        model.addAttribute("postList", boardService.postList(postVO));
        return "board/list";
    }

    @GetMapping("/detail")
    public String detailView(PostVO postVO, Model model) {
        PostVO detailView = boardService.postView(postVO);
        if(detailView == null) {
            throw new RuntimeException("게시글을 찾을 수 없습니다.");
        }
        model.addAttribute("detailView", detailView);
        return "board/detail";
    }

    @GetMapping("/edit")
    public String editView() {
        return "board/edit";
    }

    @PostMapping("/edit")
    public String edit(PostVO postVO) {
        boardService.postWrite(postVO);
        return "redirect:/board/list";
    }

    @GetMapping("/update")
    public String updateView(PostVO postVO, Model model) {
        PostVO detailView = boardService.postView(postVO);
        model.addAttribute("detailView", detailView);
        return "board/edit";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "redirect:/board/list";
    }
}
