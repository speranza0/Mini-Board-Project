package com.board.webmvc.controller.main;

import com.board.webmvc.service.user.UserVO;
import org.apache.catalina.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @GetMapping("/")
    public String main(@SessionAttribute(name = "loginUser", required = false) UserVO loginUser, Model model) {
        //세션 유지
        model.addAttribute("loginUser", loginUser);
        return "index";
    }
}
