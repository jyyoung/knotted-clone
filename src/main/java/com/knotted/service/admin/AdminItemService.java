package com.knotted.service.admin;

import com.knotted.dto.ItemDTO;
import com.knotted.dto.ItemFormDTO;
import com.knotted.dto.ItemImageDTO;
import com.knotted.entity.Item;
import com.knotted.entity.ItemImage;
import com.knotted.repository.admin.AdminItemImageRepository;
import com.knotted.repository.admin.AdminItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminItemService {

    private final AdminItemRepository adminItemRepository;
    private final AdminItemImageRepository adminItemImageRepository;
    private final AdminItemImageService adminItemImageService;

    // 상품 등록 메소드
    public void saveItem(ItemFormDTO itemFormDTO, MultipartFile itemImageFile) throws Exception{

        // 상품을 먼저 등록한다
        Item item = itemFormDTO.createItem(); // 받은 itemFormDTO 객체를 엔티티로 변환 후 저장
        adminItemRepository.save(item);

        // 이미지 엔티티를 생성하고 해당 엔티티 안에 FK로 둔 엔티티를 설정한다
        ItemImage itemImage = new ItemImage();
        itemImage.setItem(item); // 위에서 저장한 엔티티를 FK로 넣어준다

        adminItemImageService.saveItemImage(itemImage, itemImageFile);

        // 여기까지 정상적으로 됐으면 매장 및 매장 이미지 업로드, 매장 이미지 DB까지 저장 완료.
    }

    // 모든 상품 조회 메소드
    public List<ItemDTO> getAllItem(){
        List<Item> itemList = adminItemRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<ItemDTO> itemDTOList = new ArrayList<>();

        for(Item item : itemList){
            ItemDTO itemDTO = ItemDTO.of(item);
            // 해당 Item로 ItemImage를 찾아내서 추가한다
            ItemImage itemImage = adminItemImageRepository.findByItemId(item.getId()); // 매장 이미지 엔티티 조회

            if(itemImage != null){ // 해당 이미지가 있으면
                ItemImageDTO itemImageDTO = ItemImageDTO.of(itemImage);
                itemDTO.setItemImageDTO(itemImageDTO); // 매장 이미지 DTO를 매장 DTO에 세팅
            }

            itemDTOList.add(itemDTO);
        }

        return itemDTOList;
    }
}
