package com.board.webmvc.controller.board;

import com.board.webmvc.service.board.PageVO;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

public class BoardParam {

    // 검색 클래스
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    public static class Search extends PageVO {
        private String searchType;
        private String searchValue;

    }

    // 게시글 상세보기
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Getter @Setter
    public static class Post {

        private int idx;

        private int boardIdx;

        private int userIdx;

        private String title;

        private String content;

        private Date regdate;

        private int hit;

        //게시물 정보에 join 한 유저정보
        private String id;

        private String nickname;

        //게시물 정보에 join 한 첨부파일 정보
        private String uuid;

        private String originname;

        private int size;

        private String type;

        private String path;
    }

    // 이전글 다음글
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    public static class PreNext {
        private String postType;

        private int idx;

        private String title;

        private Date regdate;
    }

    // 등록
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    public static class Create {
        private int idx;

        private int boardIdx;

        private int userIdx;

        @NotEmpty(message = "제목은 필수 입력 값입니다.")
        private String title;

        @NotEmpty(message = "내용은 필수 입력 값입니다.")
        private String content;

        private Date regdate;

        //첨부파일을 위한 멀티파트파일
        private MultipartFile file;
    }

    // 수정
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter @Setter
    public static class Update {
        private int idx;

        private int boardIdx;

        private int userIdx;

        @NotEmpty(message = "제목은 필수 입력 값입니다.")
        private String title;

        @NotEmpty(message = "내용은 필수 입력 값입니다.")
        private String content;

        private Date updatetime;

        //게시물 정보에 join 한 첨부파일 정보
        private String uuid;

        private String originname;

        private int size;

        private String type;

        // 게시물 기존 첨부파일 삭제 여부
        private String fileDeleteYn;

        //첨부파일을 위한 멀티파트파일
        private MultipartFile file;
    }
}
