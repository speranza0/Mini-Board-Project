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
    public String loginView(Model model, @RequestParam(value = "redirectURL", required = false) String redirectURL) {
        model.addAttribute("redirectURL", redirectURL);
        log.info("Get redirectURL = {}", redirectURL);
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute UserVO user, @RequestParam(defaultValue = "/") String redirectURL, Model model, HttpServletRequest request) {

        try {
            UserVO loginUser = userService.login(user.getId(), user.getPassword());
            userService.updateLatestLogin(loginUser.getIdx());
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", loginUser);
            log.info("redirectURL = {}", redirectURL);
            return "redirect:" + redirectURL;
        } catch (RuntimeException e) {
            model.addAttribute("exception", e.getMessage());
            return "user/login";
        }
    }

    @RequestMapping("/adminLogin")
    public String adminLogin(HttpServletRequest request) {
        UserVO admin = userService.login("admin", "123456");
        userService.updateLatestLogin(admin.getIdx());
        HttpSession session = request.getSession();
        session.setAttribute("loginUser", admin);
        return "redirect:/";
    }

    @RequestMapping("/testerLogin")
    public String testerLogin(HttpServletRequest request) {
        UserVO tester = userService.login("tester", "123456");
        userService.updateLatestLogin(tester.getIdx());
        HttpSession session = request.getSession();
        session.setAttribute("loginUser", tester);
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
