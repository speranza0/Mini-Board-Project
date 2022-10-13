package com.board.webmvc.controller.user;

import com.board.webmvc.service.user.UserMapper;
import com.board.webmvc.service.user.UserService;
import com.board.webmvc.service.user.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;


    @GetMapping("/login")
    public String loginView() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute UserVO user, Model model, HttpServletRequest request) {

        try {
            UserVO loginUser = userService.login(user.getId(), user.getPassword());
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", loginUser);
            return "redirect:/";
        } catch (RuntimeException e) {
            model.addAttribute("exception", e.getMessage());
            return "user/login";
        }
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
