package com.board.webmvc.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@ControllerAdvice
public class ApiExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalExHandle(IllegalArgumentException e) {
        log.error("[exceptionHandle] ex", e);
        return "error/4xx";
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public String exHandle(Exception e) {
        log.error("[exceptionHandle] ex", e);
        return "error/500";
    }
}
