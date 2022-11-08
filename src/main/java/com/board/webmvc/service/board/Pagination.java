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
        endPage = (int)(Math.ceil(pageVO.getPageIndex() / (double) displayPageNum) * displayPageNum);
        startPage = (endPage - displayPageNum) + 1;
        if(startPage <= 0) {
            startPage = 1;
        }

        int tempEndPage = (int)(Math.ceil(totalRecordCount / (double) pageVO.getRecordCountPerPage()));
        if(endPage > tempEndPage) {
            endPage = tempEndPage;
        }

        xprev = startPage == 1 ? false : true;
        xnext = endPage * pageVO.getRecordCountPerPage() < totalRecordCount ? true : false;
    }
}
