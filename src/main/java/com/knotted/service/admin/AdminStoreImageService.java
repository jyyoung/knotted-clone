package com.knotted.service.admin;

import com.knotted.repository.admin.AdminStoreImageRepository;
import com.knotted.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminStoreImageService {

    @Value("${storeImageLocation}") // application.properties에서 값 가져와서 변수에 저장
    private String storeImageLocation;

    private final AdminStoreImageRepository adminStoreImageRepository;

    private final FileService fileService;
}
