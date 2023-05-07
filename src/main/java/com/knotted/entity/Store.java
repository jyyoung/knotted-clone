package com.knotted.entity;

// 매장 정보 엔티티

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "store")
@Data
public class Store extends BaseEntity{

    // 매장 ID
    @Id
    @Column(name = "store_id")
    private Long id;

    // 매장명
    @Column(name = "store_name")
    private String name;

    // 전화번호
    @Column(name = "store_phone_number")
    private String phoneNumber;

    // 주소
    @Column(name = "store_address")
    private String address;

    // 영업시작시간
    @Column(name = "store_open_time")
    private LocalDateTime openTime;

    // 영업종료시간
    @Column(name = "store_close_time")
    private LocalDateTime closeTime;

    // 매장 설명
    @Column(name = "store_description")
    private String description;

}
