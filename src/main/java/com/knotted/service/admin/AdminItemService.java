package com.knotted.service.admin;

import com.knotted.dto.ItemDTO;
import com.knotted.dto.ItemFormDTO;
import com.knotted.dto.ItemImageDTO;
import com.knotted.entity.Item;
import com.knotted.entity.ItemImage;
import com.knotted.repository.admin.AdminItemImageRepository;
import com.knotted.repository.admin.AdminItemRepository;
import jakarta.persistence.EntityNotFoundException;
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

        // 여기까지 정상적으로 됐으면 상품 및 상품 이미지 업로드, 상품 이미지 DB까지 저장 완료.
    }

    // 모든 상품 조회 메소드
    public List<ItemDTO> getAllItems(){
        List<Item> itemList = adminItemRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<ItemDTO> itemDTOList = new ArrayList<>();

        for(Item item : itemList){
            ItemDTO itemDTO = ItemDTO.of(item);
            // 해당 Item로 ItemImage를 찾아내서 추가한다
            ItemImage itemImage = adminItemImageRepository.findByItemId(item.getId()); // 상품 이미지 엔티티 조회

            if(itemImage != null){ // 해당 이미지가 있으면
                ItemImageDTO itemImageDTO = ItemImageDTO.of(itemImage);
                itemDTO.setItemImageDTO(itemImageDTO); // 상품 이미지 DTO를 상품 DTO에 세팅
            }

            itemDTOList.add(itemDTO);
        }

        return itemDTOList;
    }

    // 상품 삭제 메소드
    public void deleteItem(Long itemId) throws Exception {
        Item item = adminItemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // 일단 삭제 전 해당 이미지 파일도 같이 제거해야 함 (어차피 실제 이미지도 제거해야 하니까 굳이 양방향 매핑 하지 않았음)
        ItemImage itemImage = adminItemImageRepository.findByItemId(item.getId());

        // 상품 이미지 파일 및 DB 먼저 제거
        adminItemImageService.deleteItemImage(itemImage);

        // 정상적으로 됐으면 상품 제거
        adminItemRepository.delete(item);
    }
    
    // 상품 읽는 메소드 (이미지까지 포함)
    public ItemFormDTO getItem(Long itemId){
        Item item = adminItemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // 애초에 정상적으로 찾아졌으면 여기로 넘어옴
        ItemFormDTO itemFormDTO = ItemFormDTO.of(item);
        ItemImage itemImage = adminItemImageRepository.findByItemId(item.getId());
        ItemImageDTO itemImageDTO = ItemImageDTO.of(itemImage);
        itemFormDTO.setItemImageDTO(itemImageDTO);

        return itemFormDTO;
    }


    // 상품 수정 메소드
    public void updateItem(ItemFormDTO itemFormDTO, MultipartFile itemImageFile) throws Exception{

        // 상품을 먼저 수정한다
        Item item = adminItemRepository.findById(itemFormDTO.getId())
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDTO);

        // 새로 올라온 이미지가 있으면 기존 거 삭제 후 등록
        if(!itemImageFile.isEmpty()){
            // 기존 이미지를 찾는다
            ItemImage itemImage = adminItemImageRepository.findByItemId(item.getId()); // 상품 이미지 엔티티 조회

            // 이미지가 있으면 기존 이미지부터 제거해준다
            if(itemImage != null){
                adminItemImageService.deleteItemImage(itemImage);
            }

            // ItemImage를 생성 후 엔티티만 넣어주고,
            ItemImage newItemImage = new ItemImage(); // ItemImage 엔티티 생성
            newItemImage.setItem(item); // 위에서 저장한 엔티티를 FK로 넣어준다

            // 새 이미지 등록
            adminItemImageService.saveItemImage(newItemImage, itemImageFile);
        }
    }
}
