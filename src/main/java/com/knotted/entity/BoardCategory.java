package com.knotted.entity;

import jakarta.persistence.*;
import lombok.Data;

// 게시판 카테고리 엔티티

@Entity
@Table(name = "board_category")
@Data
public class BoardCategory extends BaseEntity{

    // 게시판 카테고리 ID
    @Id
    @GeneratedValue
    @Column(name = "board_category_id")
    private Long id;

    // 카테고리명 (영문, 유니크)
    @Column(name = "board_category_category", unique = true)
    private String category;

    @Column(name = "board_category_name")
    private String name;
}





