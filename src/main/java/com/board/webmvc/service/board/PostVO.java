package com.board.webmvc.service.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class PostVO extends PageVO{

    private int idx;

    private int boardIdx;

    private int userIdx;

    private String title;

    private String content;

    private Date regdate;

    private Date updatetime;

    private int hit;

    private String id;

    private String nickname;

    private String searchType;

    private String searchValue;
}
