package com.knotted.service;


import com.knotted.dto.StoreItemDTO;
import com.knotted.entity.StoreItem;
import com.knotted.repository.StoreItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StoreItemService {

    private final StoreItemRepository storeItemRepository;
    private final ItemService itemService;

    // 해당 매장의 특정 매장 상품을 조회함
    public StoreItemDTO getStoreItemByStoreIdAndItemId(Long storeId, Long itemId) {

        StoreItem storeItem = storeItemRepository.findByStoreIdAndItemId(storeId, itemId);
        StoreItemDTO storeItemDTO = new StoreItemDTO();

        if(storeItem != null){
            storeItemDTO = StoreItemDTO.of(storeItem);
        }

        return storeItemDTO;
    }

    // 해당 매장의 모든 매장 상품을 조회함
    public List<StoreItemDTO> getStoreItems(Long storeId){

        List<StoreItemDTO> storeItemDTOList = new ArrayList<>();

        List<StoreItem> storeItemList = storeItemRepository.findByStoreId(storeId);

        for(StoreItem storeItem : storeItemList){
            StoreItemDTO storeItemDTO = StoreItemDTO.of(storeItem);

            // 아이템 정보를 포함시킨다
            storeItemDTO.setItemDTO(itemService.convertToItemDTO(storeItem.getItem()));

            storeItemDTOList.add(storeItemDTO);
        }

        return storeItemDTOList;
    }

}
