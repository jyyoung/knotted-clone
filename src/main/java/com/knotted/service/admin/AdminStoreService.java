package com.knotted.service.admin;

import com.knotted.dto.StoreFormDTO;
import com.knotted.entity.Store;
import com.knotted.entity.StoreImage;
import com.knotted.repository.admin.AdminStoreImageRepository;
import com.knotted.repository.admin.AdminStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminStoreService {

    private final AdminStoreRepository adminStoreRepository;
    private final AdminStoreImageRepository adminStoreImageRepository;
    private final AdminStoreImageService adminStoreImageService;

    // 파일을 다루는 거라 Exception 뜰 수도 있어서 Exception 던져 놓음. 오류가 발생하면 이 메소드를 호출하는 곳에서 예외처리를 할 것임
    // StoreFormDTO와 MultipartFile을 받아서 매장과 매장 이미지를 DB에 같이 등록하기 위함이다. 둘 중 하나가 실패하면 @Transactional에 의해 롤백된다.
    public void saveStore(StoreFormDTO storeFormDTO, MultipartFile storeImageFile) throws Exception{

        // 매장을 먼저 등록한다
        Store store = storeFormDTO.createStore(); // 받은 StoreFormDTO 객체를 엔티티로 변환 후 저장
        adminStoreRepository.save(store);

        // StoreImage를 생성 후 엔티티만 넣어주고,
        StoreImage storeImage = new StoreImage(); // StoreImage 엔티티 생성
        storeImage.setStore(store); // 위에서 저장한 엔티티를 FK로 넣어준다

        adminStoreImageService.saveStoreImage(storeImage, storeImageFile);

        // 여기까지 정상적으로 됐으면 매장 및 매장 이미지 업로드, 매장 이미지 DB까지 저장 완료.
    }
}



