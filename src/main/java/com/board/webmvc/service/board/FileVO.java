package com.board.webmvc.service.board;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class FileVO {

    private int idx;

    private int postIdx;

    private int boardIdx;

    private String originname;

    private String uuid;

    private int size;

    private String type;

    private String path;

    private MultipartFile uploadFile;
}
