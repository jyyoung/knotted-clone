package com.knotted.service.admin;

import com.knotted.repository.admin.AdminItemImageRepository;
import com.knotted.repository.admin.AdminItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminItemService {

    private final AdminItemRepository adminItemRepository;
    private final AdminItemImageRepository adminItemImageRepository;
    private final AdminItemImageService adminItemImageService;

}
