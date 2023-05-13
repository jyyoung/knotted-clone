package com.knotted.repository.admin;

import com.knotted.entity.ItemImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminItemImageRepository extends JpaRepository<ItemImage, Long> {

    // 매장 ID로 해당 이미지 조회
    ItemImage findByItemId(Long itemId);

}
