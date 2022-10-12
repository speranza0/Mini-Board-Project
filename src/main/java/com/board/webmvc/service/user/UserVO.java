package com.board.webmvc.service.user;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UserVO {

    private int idx;

    private String id;

    private int level;

    private String password;

    private String email;

    private String name;

    private String nickname;

    private String phone;

    private String latestLogin;

    private String regdate;

}
