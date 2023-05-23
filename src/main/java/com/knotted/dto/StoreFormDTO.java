package com.knotted.dto;

import com.knotted.entity.Store;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;

@Data
public class StoreFormDTO {

    // 매장 ID
    private Long id;

    // 매장명
    @NotBlank(message = "매장명은 필수 입력값입니다")
    private String name;

    // 전화번호
    @NotBlank(message = "전화번호는 필수 입력값입니다")
    @Pattern(regexp = "^[0-9]*$")
    private String phoneNumber;

    // 우편번호
    @NotBlank(message = "우편번호는 필수 입력값입니다")
    @Size(max = 6, message = "우편번호는 최대 6자리입니다")
    @Pattern(regexp = "^[0-9]*$")
    private String postcode;

    // 주소
    @NotBlank(message = "주소는 필수 입력값입니다")
    private String address;

    // 주소 좌표
    private Point coordinate;

    // 위도 데이터 전달용
    private Double latitude;
    // 경도 데이터 전달용
    private Double longitude;

    // 영업시작시간
    @NotBlank(message = "영업시작시간은 필수 입력값입니다")
    @Pattern(regexp = "^[0-9]*$")
    @Size(min = 4, max = 4) // 4글자로 맞추기
    private String openTime;

    // 영업종료시간
    @NotBlank(message = "영업종료시간은 필수 입력값입니다")
    @Pattern(regexp = "^[0-9]*$")
    @Size(min = 4, max = 4)
    private String closeTime;

    // 매장 설명
    private String description;

    // 매장 이미지
    private StoreImageDTO storeImageDTO;

    // 엔티티 <-> DTO간 변환에 사용할 ModelMapper 객체
    private static ModelMapper modelMapper = new ModelMapper();

    // DTO -> 엔티티 변환
    public Store createStore(){
        return modelMapper.map(this, Store.class);
    }

    // 엔티티 -> DTO 변환
    public static StoreFormDTO of(Store store){
        return modelMapper.map(store, StoreFormDTO.class);
    }
}
