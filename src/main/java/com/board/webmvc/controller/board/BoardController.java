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

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/list")
    public String listView(PostVO param, Model model) {
        model.addAttribute("postList", boardService.postList(param));
        return "board/list";
    }

    @GetMapping("/detail")
    public String detailView() {
        return "board/detail";
    }

    @GetMapping("/edit")
    public String editView() {
        return "board/edit";
    }

    @PostMapping("/edit")
    public String edit(PostVO param) {
        boardService.postWrite(param);
        return "board/list";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "board/list";
    }
}
