package com.board.webmvc.service.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class UserVO {

    private int userIdx;

    private String userId;

    private int userLevel;

    private String userPassword;

    private String userEmail;

    private String userName;

    private String userNickname;

    private String latestLogin;

    private String regDate;

}
