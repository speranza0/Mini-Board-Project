package com.board.webmvc.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
public class LoginCheckFilter implements Filter {
    private static final String[] whitelist = {"/", "/user/*", "/board/list", "/board/detail", "/assets/*", "/script/*", "/style/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String query = httpRequest.getQueryString();

        if (query != null) {
            query = URLEncoder.encode(query, "UTF-8");
        }

        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try {
            log.info("인증 체크 필터 시작 {}", requestURI);
            if(isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행 {}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if(session == null || session.getAttribute("loginUser") == null) {
                    log.info("미인증 사용자 요청 {}", requestURI);
                    //로그인으로 redirect
                    log.info("filter query = {}", query);
                    httpResponse.sendRedirect("/user/login?redirectURL=" + requestURI + "?" + query);
                    return; // 여기가 중요, 미인증 사용자는 다음으로 진행하지 않고 끝
                }
            }
            chain.doFilter(request, response);
        } catch (Exception e) {
            throw e; //예외 로깅 가능하지만, 톰캣까지 예외를 보내줘야함
        } finally {
            log.info("인증 체크 필터 종료 {}", requestURI);
        }
    }

    /**
     * 화이트 리스트의 경우 인증 체크 X
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }
}
