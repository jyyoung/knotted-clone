package com.knotted.repository.admin;

import com.knotted.entity.StoreImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminStoreImageRepository extends JpaRepository<StoreImage, Long> {

    // 매장 ID로 해당 이미지 조회
    StoreImage findByStoreId(Long storeId);

}
