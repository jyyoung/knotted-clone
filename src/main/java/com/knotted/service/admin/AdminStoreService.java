package com.knotted.service.admin;

import com.knotted.repository.admin.AdminStoreImageRepository;
import com.knotted.repository.admin.AdminStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminStoreService {

    private final AdminStoreRepository adminStoreRepository;
    private final AdminStoreImageRepository adminStoreImageRepository;
    private final AdminStoreImageService adminStoreImageService;


}
