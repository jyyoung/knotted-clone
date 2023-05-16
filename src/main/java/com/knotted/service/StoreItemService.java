package com.knotted.service;


import com.knotted.dto.StoreItemDTO;
import com.knotted.entity.StoreItem;
import com.knotted.repository.StoreItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreItemService {

    private final StoreItemRepository storeItemRepository;

    // 해당 매장의 매장 상품을 조회함
    public StoreItemDTO getStoreItemByStoreIdAndItemId(Long storeId, Long itemId) {

        StoreItem storeItem = storeItemRepository.findByStoreIdAndItemId(storeId, itemId);
        StoreItemDTO storeItemDTO = new StoreItemDTO();

        if(storeItem != null){
            storeItemDTO = StoreItemDTO.of(storeItem);
        }

        return storeItemDTO;
    }

}
