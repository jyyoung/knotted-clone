package com.knotted.service.admin;


import com.knotted.entity.BoardImage;
import com.knotted.repository.admin.AdminBoardImageRepository;
import com.knotted.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.io.File;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminBoardImageService {

    @Value("${boardImageLocation}") // application.properties에서 값 가져와서 변수에 저장
    private String boardImageLocation;

    private final AdminBoardImageRepository adminBoardImageRepository;
    private final FileService fileService;

    // 게시글 이미지 DB에 저장. (저장하기 전 업로드부터 실시함)
    public void saveBoardImage(BoardImage boardImage, MultipartFile boardImageFile) throws Exception{

        // BoardImage를 업데이트하기 위한 변수들
        String originalImageName = boardImageFile.getOriginalFilename(); // MultipartFile 객체를 이용해 업로드된 파일명 가져옴
        String imageName = "";
        String imageUrl = "";

        if(!StringUtils.isEmpty(originalImageName)){ // 파일 이름이 비지 않았다면
            // 매개변수들 넘겨서 업로드한다. 중복이 해결된 파일명을 imageName으로 저장함
            imageName = fileService.uploadFile(boardImageLocation, originalImageName, boardImageFile.getBytes());
            imageUrl = "/images/board/" + imageName;
        }

        // 정상적으로 업로드됐으면 게시글 이미지 DB에 저장
        boardImage.updateBoardImage(imageName, originalImageName, imageUrl);
        adminBoardImageRepository.save(boardImage);
    }

    // 게시글 이미지 파일 제거 및 DB에서 제거
    public void deleteBoardImage(BoardImage boardImage) throws Exception{
        String imageUrl = boardImageLocation + "/" + boardImage.getImageName();

        File file = new File(imageUrl);

        if(file.exists()){ // 파일이 존재하면
            file.delete(); // 파일 제거
        }

        // 파일 제거했으면 게시글 이미지 DB 제거
        adminBoardImageRepository.delete(boardImage);
    }
}
