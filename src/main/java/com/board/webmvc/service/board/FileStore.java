package com.board.webmvc.service.board;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class FileStore {

    @Autowired
    private BoardService boardService;

    @Value("${file.dir}")
    private String fileDir;

    // 파일 path
    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    // 파일 업로드 메서드
    public PostVO uploadFile(MultipartFile file) throws ServletException, IOException {
        if (file.isEmpty()) {
            return null;
        }
        String originFilename = createOriginFileName(file.getOriginalFilename());
        String fileExt = fileType(file.getOriginalFilename());
        int fileSize = (int) file.getSize();
        String storeFileName = createStoreFileName(originFilename);
        file.transferTo(new File(getFullPath(storeFileName)));

        return FileVO.builder()
                .originname(originFilename)
                .path(getFullPath(storeFileName))
                .type(fileExt)
                .size(fileSize)
                .uuid(storeFileName)
                .build();
    }

    // 원본 파일 이름 메서드
    private String createOriginFileName(String originFilename) {
        int pos = originFilename.lastIndexOf(".");
        return originFilename.substring(0, pos);
    }

    // UUID 생성 메서드
    private String createStoreFileName(String originFilename) {
        String type = extractType(originFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    // 파일 확장자 저장 메서드
    private String fileType(String originFilename) {
        String type = extractType(originFilename);
        return type;
    }

    // 파일 확장자만 추출하는 메서드
    private String extractType(String originFilename) {
        int pos = originFilename.lastIndexOf(".");
        return originFilename.substring(pos + 1);
    }

    // 첨부파일 다운로드 메서드
    public ResponseEntity<Resource> downloadAttach(@PathVariable FileVO param) throws MalformedURLException {
        FileVO vo = boardService.attachFileDown(param);
        String uploadFileName = vo.getOriginname();
        String fileType = vo.getType();

        UrlResource resource = new UrlResource("file:" + vo.getPath());
        String encodeUploadFileName = UriUtils.encode(uploadFileName + "." + fileType, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodeUploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(resource);
    }
}
