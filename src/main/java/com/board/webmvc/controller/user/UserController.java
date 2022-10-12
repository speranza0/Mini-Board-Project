package com.board.webmvc.controller.user;

import com.board.webmvc.service.user.UserMapper;
import com.board.webmvc.service.user.UserService;
import com.board.webmvc.service.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Slf4j
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
    public String login(@Validated @ModelAttribute UserVO user, BindingResult bindingResult, HttpServletRequest request) {
        if(bindingResult.hasErrors()) {
            return "user/login";
        }
        UserVO loginUser = userService.login(user.getId());
        log.info("id = {}", user.getId());
        log.info("pw = {}", user.getPassword());
        log.info("login = {}", loginUser);

        if(loginUser == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "user/login";
        }

        HttpSession session = request.getSession();
        session.setAttribute("loginUser", loginUser);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }

    @GetMapping("/join")
    public String joinView() {
        return "user/join";
    }

    @PostMapping("/join")
    public String join(UserVO user, Model model) {
        userService.joinUser(user);
        model.addAttribute("joinUser", user);
        return "user/login";
    }
}
