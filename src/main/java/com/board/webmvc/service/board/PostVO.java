package com.board.webmvc.service.board;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
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

    private MultipartFile uploadFile;
}
