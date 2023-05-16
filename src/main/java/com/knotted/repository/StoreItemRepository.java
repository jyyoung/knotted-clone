package com.knotted.repository;

import com.knotted.entity.StoreItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreItemRepository extends JpaRepository<StoreItem, Long> {

    // 해당 매장의 상품 전부 조회
    List<StoreItem> findByStoreId(Long storeId);

    // 해당 매장의 특정 상품 조회
    StoreItem findByStoreIdAndItemId(Long storeId, Long itemId);

}
