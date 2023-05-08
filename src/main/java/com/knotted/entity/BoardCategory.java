package com.knotted.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

// 게시판 카테고리 엔티티

@Entity
@Table(name = "board_category")
@Data
public class BoardCategory extends BaseEntity{

    // 게시판 카테고리 ID
    @Id
    @Column(name = "board_category_id")
    private Long id;

    // 카테고리명
    @Column(name = "board_category_name")
    private String name;
}





