package com.board.webmvc.service.board;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BoardNumVO {
    private int idx;

    private String name;

    private String type;
}