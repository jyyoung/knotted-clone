package com.knotted.dto;


import com.knotted.entity.Store;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;

// 매장 정보를 화면에 뿌려주기 위한 DTO
@Data
public class StoreDTO {

    // 매장 ID
    private Long id;

    // 매장명
    private String name;

    // 전화번호
    private String phoneNumber;

    // 우편번호
    private String postcode;

    // 주소
    private String address;

    // 주소 좌표
    private Point coordinate; // 이거 넣으니까 순환참조 남!

    // 영업시작시간
    private String openTime;

    // 영업종료시간
    private String closeTime;

    // 매장 설명
    private String description;

    // 매장 이미지
    private StoreImageDTO storeImageDTO;
    
    // 회원과 매장 사이의 거리 (m, 미터)
    private Long distance; // 반올림하여 저장할 것임

    // 엔티티 <-> DTO간 변환에 사용할 ModelMapper 객체
    private static ModelMapper modelMapper = new ModelMapper();

    // 엔티티를 DTO로 변환하여 리턴
    public static StoreDTO of(Store store){
        return modelMapper.map(store, StoreDTO.class);
    }
}
