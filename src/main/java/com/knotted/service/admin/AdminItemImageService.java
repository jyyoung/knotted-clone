package com.knotted.service.admin;

import com.knotted.entity.ItemImage;
import com.knotted.repository.admin.AdminItemImageRepository;
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
public class AdminItemImageService {

    @Value("${itemImageLocation}") // application.properties에서 값 가져와서 변수에 저장
    private String itemImageLocation;

    private final AdminItemImageRepository adminItemImageRepository;
    private final FileService fileService;

    // 상품 이미지 DB에 저장. (저장하기 전 업로드부터 실시함)
    public void saveItemImage(ItemImage itemImage, MultipartFile itemImageFile) throws Exception{

        // ItemImage를 업데이트하기 위한 변수들
        String originalImageName = itemImageFile.getOriginalFilename(); // MultipartFile 객체를 이용해 업로드된 파일명 가져옴
        String imageName = "";
        String imageUrl = "";

        if(!StringUtils.isEmpty(originalImageName)){ // 파일 이름이 비지 않았다면
            // 매개변수들 넘겨서 업로드한다. 중복이 해결된 파일명을 imageName으로 저장함
            imageName = fileService.uploadFile(itemImageLocation, originalImageName, itemImageFile.getBytes());
            imageUrl = "/images/item/" + imageName;
        }

        // 정상적으로 업로드됐으면 상품 이미지 DB에 저장
        itemImage.updateItemImage(imageName, originalImageName, imageUrl);
        adminItemImageRepository.save(itemImage);
    }
    
    // 상품 이미지 파일 제거 및 DB에서 제거
    public void deleteItemImage(ItemImage itemImage) throws Exception{
        String imageUrl = itemImageLocation + "/" + itemImage.getImageName();

        File file = new File(imageUrl);

        if(file.exists()){ // 파일이 존재하면
            file.delete(); // 파일 제거
        }

        // 파일 제거했으면 상품 이미지 DB 제거
        adminItemImageRepository.delete(itemImage);
    }

}
