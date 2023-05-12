package com.knotted.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    // 파일 업로드 메소드
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
        UUID uuid = UUID.randomUUID(); // 파일명 중복 해결용

        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 확장자 추출
        String fileName = uuid.toString() + extension; // 저장할 파일명
        String fullUploadPath = uploadPath + "/" + fileName;

        // 여긴 따로 추가한 부분. 만약 uploadPath가 존재하지 않으면 거기까지의 디렉터리를 만들어준다.
        File uploadDir = new File(uploadPath);
        if(!uploadDir.exists()){
            uploadDir.mkdirs();
        }

        FileOutputStream fos = new FileOutputStream(fullUploadPath);

        fos.write(fileData);
        fos.flush();
        fos.close();

        log.info(fullUploadPath + "에 파일을 업로드했습니다");
        return fileName; // Exception 안 뜨고 정상적으로 보내졌으면 완성된 파일명 반환
    }

    // 파일 삭제 메소드
    public void deleteFile(String filePath) throws Exception{
        File deleteFile = new File(filePath); // 지울 파일을 File 객체로 생성

        if(deleteFile.exists()){
            deleteFile.delete();
            log.info(filePath + "파일을 삭제하였습니다");
        }else{
            log.info(filePath + "파일이 존재하지 않습니다");
        }
    }
}
