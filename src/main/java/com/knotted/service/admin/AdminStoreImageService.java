package com.knotted.service.admin;

import com.knotted.entity.StoreImage;
import com.knotted.repository.admin.AdminStoreImageRepository;
import com.knotted.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminStoreImageService {

    @Value("${storeImageLocation}") // application.properties에서 값 가져와서 변수에 저장
    private String storeImageLocation;

    private final AdminStoreImageRepository adminStoreImageRepository;
    private final FileService fileService;

    // 매장 이미지 DB에 저장. (저장하기 전 업로드부터 실시함)
    public void saveStoreImage(StoreImage storeImage, MultipartFile storeImageFile) throws Exception{
        
        // StoreImage를 업데이트하기 위한 변수들
        String originalImageName = storeImageFile.getOriginalFilename(); // MultipartFile 객체를 이용해 업로드된 파일명 가져옴
        String imageName = "";
        String imageUrl = "";

        if(!StringUtils.isEmpty(originalImageName)){ // 파일이 있다면
            // 매개변수들 넘겨서 업로드한다. 중복이 해결된 파일명을 imageName으로 저장함
            imageName = fileService.uploadFile(storeImageLocation, originalImageName, storeImageFile.getBytes());
            imageUrl = "/images/store/" + imageName;
        }

        // 정상적으로 업로드됐으면 매장 이미지 DB에 저장
        storeImage.updateStoreImage(imageName, originalImageName, imageUrl);
        adminStoreImageRepository.save(storeImage);
    }
}
