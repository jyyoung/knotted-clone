package com.knotted.service.admin;

import com.knotted.dto.StoreDTO;
import com.knotted.dto.StoreFormDTO;
import com.knotted.dto.StoreImageDTO;
import com.knotted.entity.Store;
import com.knotted.entity.StoreImage;
import com.knotted.repository.admin.AdminStoreImageRepository;
import com.knotted.repository.admin.AdminStoreRepository;
import com.knotted.util.TimeUtils;
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

    // 매장 수정 메소드
    public void updateStore(StoreFormDTO storeFormDTO, MultipartFile storeImageFile) throws Exception{

        // 매장 등록 전 시간을 먼저 가운데 :를 넣어준다 (util 패키지의 클래스로 해당 기능을 구현해놓았음)
        storeFormDTO.setOpenTime(TimeUtils.addColonToTime(storeFormDTO.getOpenTime()));
        storeFormDTO.setCloseTime(TimeUtils.addColonToTime(storeFormDTO.getCloseTime()));

        // 매장을 먼저 수정한다
        Store store = adminStoreRepository.findById(storeFormDTO.getId())
                .orElseThrow(EntityNotFoundException::new);
        store.updateStore(storeFormDTO);
        
        // 새로 올라온 이미지가 있으면 기존 거 삭제 후 등록
        if(!storeImageFile.isEmpty()){
            // 기존 이미지를 찾는다
            StoreImage storeImage = adminStoreImageRepository.findByStoreId(store.getId()); // 매장 이미지 엔티티 조회

            // 이미지가 있으면 기존 이미지부터 제거해준다
            if(storeImage != null){
                adminStoreImageService.deleteStoreImage(storeImage);
            }

            // StoreImage를 생성 후 엔티티만 넣어주고,
            StoreImage newStoreImage = new StoreImage(); // StoreImage 엔티티 생성
            newStoreImage.setStore(store); // 위에서 저장한 엔티티를 FK로 넣어준다

            // 새 이미지 등록
            adminStoreImageService.saveStoreImage(newStoreImage, storeImageFile);
        }
    }

    // StoreDTO가 바로 여기서 필요함. 화면에 뿌려줄 땐 StoreFormDTO를 쓸 순 없지 않은가.
    // Store 엔티티를 리스트로 먼저 조회 후 이걸 DTO로 변환하여 List로 만들자
    public List<StoreDTO> getAllStore(){
        List<Store> storeList = adminStoreRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
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

    // 매장 삭제 메소드
    public void deleteStore(Long storeId) throws Exception {
        Store store = adminStoreRepository.findById(storeId)
                .orElseThrow(EntityNotFoundException::new);

        // 일단 삭제 전 해당 이미지 파일도 같이 제거해야 함 (어차피 실제 이미지도 제거해야 하니까 굳이 양방향 매핑 하지 않았음)
        StoreImage storeImage = adminStoreImageRepository.findByStoreId(store.getId());

        // 매장 이미지 파일 및 DB 먼저 제거
        adminStoreImageService.deleteStoreImage(storeImage);

        // 정상적으로 됐으면 매장 제거
        adminStoreRepository.delete(store);
    }

    // 매장 읽는 메소드 (이미지까지 포함)
    public StoreFormDTO getStore(Long storeId){
        Store store = adminStoreRepository.findById(storeId)
                .orElseThrow(EntityNotFoundException::new);

        // 애초에 정상적으로 찾아졌으면 여기로 넘어옴
        StoreFormDTO storeFormDTO = StoreFormDTO.of(store);
        StoreImage storeImage = adminStoreImageRepository.findByStoreId(store.getId());
        StoreImageDTO storeImageDTO = StoreImageDTO.of(storeImage);
        storeFormDTO.setStoreImageDTO(storeImageDTO);

        return storeFormDTO;
    }
}



