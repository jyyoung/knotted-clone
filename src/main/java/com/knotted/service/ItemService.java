package com.knotted.service;


import com.knotted.dto.ItemDTO;
import com.knotted.dto.ItemFormDTO;
import com.knotted.dto.ItemImageDTO;
import com.knotted.entity.Item;
import com.knotted.entity.ItemImage;
import com.knotted.repository.ItemImageRepository;
import com.knotted.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
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
    public List<ItemDTO> getAllItems(){
        List<Item> itemList = itemRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        List<ItemDTO> itemDTOList = new ArrayList<>();

        for(Item item : itemList){
            itemDTOList.add(convertToItemDTO(item));
        }

        return itemDTOList;
    }

    // 특정 카테고리 및 검색어로 상품 조회하는 메소드
    public List<ItemDTO> getItemsByCategoryAndSearchWord(String category, String searchWord){
        List<Item> itemList;

        if(category.equals("all")){
            if(searchWord != null && !searchWord.isEmpty()){
                itemList = itemRepository.findAllByNameContainingIgnoreCase(searchWord, Sort.by(Sort.Direction.ASC, "id"));
            }else{
                itemList = itemRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
            }
        }else{
            if(searchWord != null && !searchWord.isEmpty()){
                itemList = itemRepository.findByCategoryAndNameContainingIgnoreCase(category, searchWord, Sort.by(Sort.Direction.ASC, "id"));
            }else{
                itemList = itemRepository.findByCategory(category, Sort.by(Sort.Direction.ASC, "id"));
            }
        }

        List<ItemDTO> itemDTOList = new ArrayList<>();

        for(Item item : itemList){
            itemDTOList.add(convertToItemDTO(item));
        }

        return itemDTOList;
    }

    // 상품 엔티티를 DTO로 변환 (이 과정에서 상품 이미지 DTO도 상품 DTO에 넣음)
    // CartService에서도 쓸 거라 private에서 public으로 바꿔줌
    public ItemDTO convertToItemDTO(Item item){
        ItemDTO itemDTO = ItemDTO.of(item);
        // 해당 Item로 ItemImage를 찾아내서 추가한다
        ItemImage itemImage = itemImageRepository.findByItemId(item.getId()); // 상품 이미지 엔티티 조회

        if(itemImage != null){ // 해당 이미지가 있으면
            ItemImageDTO itemImageDTO = ItemImageDTO.of(itemImage);
            itemDTO.setItemImageDTO(itemImageDTO); // 상품 이미지 DTO를 상품 DTO에 세팅
        }

        return itemDTO;
    }

    // 상품 하나 조회하는 메소드
    public ItemFormDTO getItem(Long itemId){
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // 애초에 정상적으로 찾아졌으면 여기로 넘어옴
        ItemFormDTO itemFormDTO = ItemFormDTO.of(item);
        ItemImage itemImage = itemImageRepository.findByItemId(item.getId());
        ItemImageDTO itemImageDTO = ItemImageDTO.of(itemImage);
        itemFormDTO.setItemImageDTO(itemImageDTO);

        return itemFormDTO;
    }
}
