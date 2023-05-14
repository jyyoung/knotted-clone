package com.knotted.repository;

import com.knotted.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemImageRepository extends JpaRepository<ItemImage, Long> {

    // 상품 ID로 해당 이미지 조회
    ItemImage findByItemId(Long itemId);
}
