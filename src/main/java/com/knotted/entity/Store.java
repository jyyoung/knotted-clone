package com.knotted.entity;

import com.knotted.dto.StoreFormDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

import java.util.ArrayList;
import java.util.List;

// 매장 엔티티

@Entity
@Table(name = "store")
@Data
public class Store extends BaseEntity{

    // 매장 ID
    @Id
    @GeneratedValue
    @Column(name = "store_id")
    private Long id;

    // 매장 상품 엔티티와 일대다로 매핑
    // NPE가 발생할 우려가 있기에 명시적으로 new ArrayList<>(); 함으로써 초기화해주었다.
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<StoreItem> storeItems = new ArrayList<>();

    // 매장 삭제 시 해당 매장으로 담은 장바구니도 같이 삭제하도록 함
    // 수정 - 매장과 장바구니는 일대다 관계이다!!!
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    @OneToOne(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Cart> cartList = new ArrayList<>();

    // 매장명
    @Column(name = "store_name")
    private String name;

    // 전화번호
    @Column(name = "store_phone_number")
    private String phoneNumber;

    // 우편번호
    @Column(name = "store_postcode")
    private String postcode;

    // 주소
    @Column(name = "store_address")
    private String address;

    // 주소 좌표
    @Column(name = "store_coordinate", columnDefinition = "POINT")
    private Point coordinate;

    // 주소 위도
    @Column(name = "store_latitude")
    private Double latitude;

    // 주소 경도
    @Column(name = "store_longitude")
    private Double longitude;

    // 영업시작시간
    @Column(name = "store_open_time")
    private String openTime;

    // 영업종료시간
    @Column(name = "store_close_time")
    private String closeTime;

    // 매장 설명
    @Column(name = "store_description", length = 2000)
    private String description;

    // 매장 수정 메소드
    public void updateStore(StoreFormDTO storeFormDTO){
        this.name = storeFormDTO.getName();
        this.phoneNumber = storeFormDTO.getPhoneNumber();
        this.postcode = storeFormDTO.getPostcode();
        this.address = storeFormDTO.getAddress();
        this.openTime = storeFormDTO.getOpenTime();
        this.closeTime = storeFormDTO.getCloseTime();
        this.description = storeFormDTO.getDescription();

        // 정보 수정 시에도 latitude, longitude 필드가 만약에 있으면 수정한다
        Double latitude = storeFormDTO.getLatitude(); // 위도
        Double longitude = storeFormDTO.getLongitude(); // 경도

        if (latitude != null && longitude != null) { // 위도, 경도가 넘어온 경우에만 수정한다
            GeometryFactory geometryFactory = new GeometryFactory();
            Coordinate coordinate = new Coordinate(longitude, latitude); // 경도, 위도 순으로 입력
            Point point = geometryFactory.createPoint(coordinate); // 좌표 객체 생성

            this.latitude = latitude;
            this.longitude = longitude;
            this.coordinate = point;
        }
    }
}
