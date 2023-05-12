package com.knotted.service;

import com.knotted.dto.StoreDTO;
import com.knotted.dto.StoreFormDTO;
import com.knotted.dto.StoreImageDTO;
import com.knotted.entity.Store;
import com.knotted.entity.StoreImage;
import com.knotted.repository.StoreImageRepository;
import com.knotted.repository.StoreRepository;
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
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final StoreImageService storeImageService;

    // 모든 매장 읽어오는 메소드
    public List<StoreDTO> getAllStore(){
        List<Store> storeList = storeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<StoreDTO> storeDTOList = new ArrayList<>();

        for(Store store : storeList){
            StoreDTO storeDTO = StoreDTO.of(store);
            // 해당 Store로 StoreImage를 찾아내서 추가한다
            StoreImage storeImage = storeImageRepository.findByStoreId(store.getId()); // 매장 이미지 엔티티 조회

            if(storeImage != null){ // 해당 이미지가 있으면
                StoreImageDTO storeImageDTO = StoreImageDTO.of(storeImage);
                storeDTO.setStoreImageDTO(storeImageDTO); // 매장 이미지 DTO를 매장 DTO에 세팅
            }

            storeDTOList.add(storeDTO);
        }

        return storeDTOList;
    }

    // 매장 하나 읽어오는 메소드
    public StoreFormDTO getStore(Long storeId){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(EntityNotFoundException::new);

        // 애초에 정상적으로 찾아졌으면 여기로 넘어옴
        StoreFormDTO storeFormDTO = StoreFormDTO.of(store);
        StoreImage storeImage = storeImageRepository.findByStoreId(store.getId());
        StoreImageDTO storeImageDTO = StoreImageDTO.of(storeImage);
        storeFormDTO.setStoreImageDTO(storeImageDTO);

        return storeFormDTO;
    }
}
