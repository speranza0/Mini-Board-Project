package com.board.webmvc.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ApiExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return "error/4xx";
    }

    @ExceptionHandler(BindException.class)
    public ModelAndView illegalExHandle(HttpServletRequest request, RedirectAttributes redirectAttributes, BindException e) {
        String referer = request.getHeader("referer");
        String host = request.getHeader("host");

        String result  = referer.substring(referer.lastIndexOf(host) + host.length());

        ModelAndView mv = new ModelAndView();

        List<String> messages = new ArrayList<>();

        for (ObjectError error : e.getAllErrors()) {
            messages.add(error.getDefaultMessage());
        }

        redirectAttributes.addFlashAttribute("errors", messages);
        mv.setViewName("redirect:" + result);

        return mv;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public String exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return "error/500";
    }
}
