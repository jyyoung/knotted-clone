package com.knotted.service;


import com.knotted.dto.ItemDTO;
import com.knotted.dto.ItemImageDTO;
import com.knotted.entity.Item;
import com.knotted.entity.ItemImage;
import com.knotted.repository.ItemImageRepository;
import com.knotted.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final ItemImageService itemImageService;

    // 모든 상품 조회하는 메소드
    public List<ItemDTO> getAllItem(){
        List<Item> itemList = itemRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<ItemDTO> itemDTOList = new ArrayList<>();

        for(Item item : itemList){
            ItemDTO itemDTO = ItemDTO.of(item);
            // 해당 Item로 ItemImage를 찾아내서 추가한다
            ItemImage itemImage = itemImageRepository.findByItemId(item.getId()); // 상품 이미지 엔티티 조회

            if(itemImage != null){ // 해당 이미지가 있으면
                ItemImageDTO itemImageDTO = ItemImageDTO.of(itemImage);
                itemDTO.setItemImageDTO(itemImageDTO); // 상품 이미지 DTO를 상품 DTO에 세팅
            }

            itemDTOList.add(itemDTO);
        }

        return itemDTOList;
    }

}
