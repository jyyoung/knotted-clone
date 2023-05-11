package com.knotted.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalTime;

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

    // 영업시작시간
    @Column(name = "store_open_time")
    private String openTime;

    // 영업종료시간
    @Column(name = "store_close_time")
    private String closeTime;

    // 매장 설명
    @Column(name = "store_description")
    private String description;

}
