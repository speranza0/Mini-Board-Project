package com.board.webmvc.service.board;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter @Setter
public class Pagination {
    private PageVO pageVO;
    private int totalRecordCount; // 전체 게시물 수
    private int startPage; // 페이지 리스트의 첫 페이지 번호
    private int endPage; // 페이지 리스트의 마지막 페이지 번호
    private int displayPageNum = 10; // 페이지 리스트에 게시되는 페이지 버튼 수
    private boolean xprev; // 이전 버튼
    private boolean xnext; // 다음 버튼

    public void setTotalRecordCount(int totalRecordCount) {
        this.totalRecordCount = totalRecordCount;
        calcData();
    }

    private void calcData() {
        //끝 페이지 번호 = (현재 페이지 번호 / 화면에 보여질 페이지 번호의 갯수) * 화면에 보여질 페이지 번호의 갯수
        endPage = (int)(Math.ceil(pageVO.getPageIndex() / (double) displayPageNum) * displayPageNum);
        //시작 페이지 번호 = (끝 페이지 번호 - 화면에 보여질 페이지 번호의 갯수) + 1
        startPage = (endPage - displayPageNum) + 1;
        if(startPage <= 0) {
            startPage = 1;
        }

        //마지막 페이지 번호 = 총 게시글 수 / 한 페이지당 보여줄 게시글의 갯수
        int tempEndPage = (int)(Math.ceil(totalRecordCount / (double) pageVO.getRecordCountPerPage()));
        if(endPage > tempEndPage) {
            endPage = tempEndPage;
        }

        //이전 버튼 생성 여부 = 시작 페이지 번호 == 1 ? false : true
        xprev = startPage == 1 ? false : true;
        //다음 버튼 생성 여부 = 끝 페이지 번호 * 한 페이지당 보여줄 게시글의 갯수 < 총 게시글의 수 ? true: false
        xnext = endPage * pageVO.getRecordCountPerPage() < totalRecordCount ? true : false;
    }
}
