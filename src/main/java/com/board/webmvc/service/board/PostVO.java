package com.board.webmvc.service.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class PostVO {

    private int idx;

    private int boardIdx;

    private int userIdx;

    private String title;

    private String content;

    private String regdate;

    private String updatetime;

    private int hit;
}
