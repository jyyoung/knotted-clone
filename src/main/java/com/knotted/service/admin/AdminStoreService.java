package com.knotted.service.admin;

import com.knotted.dto.StoreDTO;
import com.knotted.dto.StoreFormDTO;
import com.knotted.dto.StoreImageDTO;
import com.knotted.entity.Store;
import com.knotted.entity.StoreImage;
import com.knotted.repository.admin.AdminStoreImageRepository;
import com.knotted.repository.admin.AdminStoreRepository;
import com.knotted.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminStoreService {

    private final AdminStoreRepository adminStoreRepository;
    private final AdminStoreImageRepository adminStoreImageRepository;
    private final AdminStoreImageService adminStoreImageService;

    // 파일을 다루는 거라 Exception 뜰 수도 있어서 Exception 던져 놓음. 오류가 발생하면 이 메소드를 호출하는 곳에서 예외처리를 할 것임
    // StoreFormDTO와 MultipartFile을 받아서 매장과 매장 이미지를 DB에 같이 등록하기 위함이다. 둘 중 하나가 실패하면 @Transactional에 의해 롤백된다.
    public void saveStore(StoreFormDTO storeFormDTO, MultipartFile storeImageFile) throws Exception{

        // 매장 등록 전 시간을 먼저 가운데 :를 넣어준다 (util 패키지의 클래스로 해당 기능을 구현해놓았음)
        storeFormDTO.setOpenTime(TimeUtils.addColonToTime(storeFormDTO.getOpenTime()));
        storeFormDTO.setCloseTime(TimeUtils.addColonToTime(storeFormDTO.getCloseTime()));

        // 매장을 먼저 등록한다
        Store store = storeFormDTO.createStore(); // 받은 StoreFormDTO 객체를 엔티티로 변환 후 저장
        adminStoreRepository.save(store);

        // StoreImage를 생성 후 엔티티만 넣어주고,
        StoreImage storeImage = new StoreImage(); // StoreImage 엔티티 생성
        storeImage.setStore(store); // 위에서 저장한 엔티티를 FK로 넣어준다

        adminStoreImageService.saveStoreImage(storeImage, storeImageFile);

        // 여기까지 정상적으로 됐으면 매장 및 매장 이미지 업로드, 매장 이미지 DB까지 저장 완료.
    }

    // StoreDTO가 바로 여기서 필요함. 화면에 뿌려줄 땐 StoreFormDTO를 쓸 순 없지 않은가.
    // Store 엔티티를 리스트로 먼저 조회 후 이걸 DTO로 변환하여 List로 만들자
    public List<StoreDTO> getAllStore(){
        List<Store> storeList = adminStoreRepository.findAll();
        List<StoreDTO> storeDTOList = new ArrayList<>();

        for(Store store : storeList){
            StoreDTO storeDTO = StoreDTO.of(store);
            // 해당 Store로 StoreImage를 찾아내서 추가한다
            StoreImage storeImage = adminStoreImageRepository.findByStoreId(store.getId()); // 매장 이미지 엔티티 조회

            if(storeImage != null){ // 해당 이미지가 있으면
                StoreImageDTO storeImageDTO = StoreImageDTO.of(storeImage);
                storeDTO.setStoreImageDTO(storeImageDTO); // 매장 이미지 DTO를 매장 DTO에 세팅
            }

            storeDTOList.add(storeDTO);
        }

        return storeDTOList;
    }
}



