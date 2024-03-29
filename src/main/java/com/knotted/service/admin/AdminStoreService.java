package com.knotted.service.admin;

import com.knotted.dto.*;
import com.knotted.entity.Store;
import com.knotted.entity.StoreImage;
import com.knotted.entity.StoreItem;
import com.knotted.repository.admin.AdminStoreImageRepository;
import com.knotted.repository.admin.AdminStoreItemRepository;
import com.knotted.repository.admin.AdminStoreRepository;
import com.knotted.util.TimeUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
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

    private final AdminItemService adminItemService;
    private final AdminStoreRepository adminStoreRepository;
    private final AdminStoreImageRepository adminStoreImageRepository;
    private final AdminStoreImageService adminStoreImageService;
    private final AdminStoreItemRepository adminStoreItemRepository;

    // 파일을 다루는 거라 Exception 뜰 수도 있어서 Exception 던져 놓음. 오류가 발생하면 이 메소드를 호출하는 곳에서 예외처리를 할 것임
    // StoreFormDTO와 MultipartFile을 받아서 매장과 매장 이미지를 DB에 같이 등록하기 위함이다. 둘 중 하나가 실패하면 @Transactional에 의해 롤백된다.
    // 매장 등록 메소드
    public void saveStore(StoreFormDTO storeFormDTO, MultipartFile storeImageFile) throws Exception{

        // 매장 등록 전 시간을 먼저 가운데 :를 넣어준다 (util 패키지의 클래스로 해당 기능을 구현해놓았음)
        storeFormDTO.setOpenTime(TimeUtils.addColonToTime(storeFormDTO.getOpenTime()));
        storeFormDTO.setCloseTime(TimeUtils.addColonToTime(storeFormDTO.getCloseTime()));

        // 매장도 좌표를 넣을 건데, 기존에 ModelMapper로 그냥 매핑해서 만들었기 때문에
        // 미리 storeFormDTO에 Point로 처리를 해서 set 해놓자
        Double latitude = storeFormDTO.getLatitude(); // 위도
        Double longitude = storeFormDTO.getLongitude(); // 경도

        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate = new Coordinate(longitude, latitude); // 경도, 위도 순으로 입력
        Point point = geometryFactory.createPoint(coordinate); // 좌표 객체 생성

        storeFormDTO.setCoordinate(point); // 생성된 좌표를 coordinate 필드에 담는다 (그럼 자동으로 매핑할 것이다)

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
    // 모든 매장 조회 메소드
    public List<StoreDTO> getAllStores(){
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

    // 모든 상품 리스트를 조회하고 해당 매장의 매장 상품을 조회하여 매장 상품 DTO 리스트를 반환함
    public List<StoreItemDTO> getStoreItemList(Long storeId){
        // Store 엔티티 조회하여 StoreDTO 만듦
        Store store = adminStoreRepository.findById(storeId)
                .orElseThrow(EntityNotFoundException::new);
        StoreDTO storeDTO = StoreDTO.of(store);

        // 일단 모든 상품 조회하여 ItemDTO 리스트 만듦
        List<ItemDTO> itemList = adminItemService.getAllItems();

        // StoreItemDTO 리스트 선언
        List<StoreItemDTO> storeItemDTOList = new ArrayList<>();

        // 상품 리스트 반복문 돌면서 StoreItemDTO 생성하여 필드 세팅해줌
        for(ItemDTO itemDTO : itemList){
            StoreItemDTO storeItemDTO = new StoreItemDTO();

            storeItemDTO.setStoreDTO(storeDTO);
            storeItemDTO.setItemDTO(itemDTO);

            // 해당 상품이 매장 상품에 있는지 확인
            StoreItem savedStoreItem = adminStoreItemRepository.findByStoreIdAndItemId(storeId, itemDTO.getId());

            if(savedStoreItem != null){ // 해당 상품이 해당 매장에 있으면
                storeItemDTO.setId(savedStoreItem.getId());
                storeItemDTO.setStock(savedStoreItem.getStock());
            }else{ // 해당 상품이 해당 매장에 없으면
                // 해당 상품의 매장 상품 엔티티를 생성함
                StoreItem storeItem = new StoreItem();
                storeItem.setStore(store);
                storeItem.setItem(itemDTO.createItem());
                storeItem.setStock(0L);
                adminStoreItemRepository.save(storeItem); // DB에 저장

//                storeItemDTO.setId(storeItemDTO.getId()); // 멍청하게 storeItem이 아니고 storeItemDTO를 가져오니 당연히 안 되지.
                storeItemDTO.setId(storeItem.getId());
                storeItemDTO.setStock(0L);
            }

            storeItemDTOList.add(storeItemDTO);
        }

        return storeItemDTOList;
    }

    // 해당 매장의 해당 상품 재고 추가하는 메소드
    public void addStock(Long storeItemId, Long stock){

        // 해당 매장 상품 엔티티 존재 확인
        StoreItem storeItem = adminStoreItemRepository.findById(storeItemId)
                .orElseThrow(EntityNotFoundException::new);

        storeItem.setStock(storeItem.getStock() + stock);
        adminStoreItemRepository.save(storeItem);
    }
}



