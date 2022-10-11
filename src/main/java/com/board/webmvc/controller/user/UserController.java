package com.board.webmvc.controller.user;

import com.board.webmvc.service.user.UserMapper;
import com.board.webmvc.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginView() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login() {
        return "/";
    }
}
