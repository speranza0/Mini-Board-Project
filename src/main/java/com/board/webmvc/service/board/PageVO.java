package com.board.webmvc.service.board;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter @Setter
public class PageVO {

    private int pageIndex; // 현재 페이지
    private int recordCountPerPage; // 한페이지당 게시되는 게시물 수
    private int pageStart; // 현재 페이지의 게시글 시작 번호

    public int getPageStart() {
        //현재 페이지의 게시글 시작 번호 = (현재 페이지 번호 - 1) * 페이지당 보여줄 게시글 갯수
        return (this.pageIndex - 1) * recordCountPerPage;
    }

    // 기본 생성자 -> 기본 세팅 : pageIndex = 1, recordCountPerPage = 10
    public PageVO() {
        this.pageIndex = 1;
        this.recordCountPerPage = 10;
    }

    public PageVO(int pageIndex, int recordCountPerPage) {
        this.pageIndex = pageIndex;
        this.recordCountPerPage = recordCountPerPage;
    }

    public void setPageIndex(int pageIndex) {
        if(pageIndex <= 0) {
            this.pageIndex = 1;
        } else {
            this.pageIndex = pageIndex;
        }
    }

    public void setRecordCountPerPage(int pageCount) {
        int cnt = this.recordCountPerPage;
        if(pageCount != cnt) {
            this.recordCountPerPage = cnt;
        } else {
            this.recordCountPerPage = pageCount;
        }
    }
}
