package com.board.webmvc.service.board;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class PostVO extends PageVO{

    //게시물 정보
    private int idx;

    private int boardIdx;

    private int userIdx;

    private String title;

    private String content;

    private Date regdate;

    private Date updatetime;

    private int hit;

    //게시물 정보에 join 한 유저정보
    private String id;

    private String nickname;

    //게시물 정보에 join 한 첨부파일 여부
    private String uuid;

    //검색 쿼리를 위한 파라미터
    private String searchType;

    private String searchValue;

    //첨부파일을 위한 멀티파트파일
    private MultipartFile file;
}
