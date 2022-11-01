package com.board.webmvc.controller.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

public class LoginParam {

    @Builder
    @Getter @Setter
    public static class Request {

        @NotEmpty(message = "아이디는 필수 항목입니다.")
        String id;

        @NotEmpty(message = "비밀번호를 필수 항목입니다.")
        String password;
    }

}
