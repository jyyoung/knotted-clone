package com.knotted.service.admin;

import com.knotted.dto.StoreFormDTO;
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
    public void saveStore(StoreFormDTO storeFormDTO, MultipartFile storeImageFile) throws Exception{

    }
}



