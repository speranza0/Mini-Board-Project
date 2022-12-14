package com.board.webmvc.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.net.URLEncoder;

@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String query = request.getQueryString();

        if (query != null) {
            query = URLEncoder.encode(query, "UTF-8");
        }

        String requestURI = request.getRequestURI();
        log.info("인증 체크 인터셉터 실행 {}", requestURI);
        HttpSession session = request.getSession();

        if(!requestURI.contains("/user/login") && (session == null || session.getAttribute("loginUser") == null)) {
            log.info("미인증 사용자 요청");
            //로그인으로 redirect
            response.sendRedirect("/user/login?redirectURL=" + requestURI + "?" + query);
            return false;
        }
        return true;
    }
}
