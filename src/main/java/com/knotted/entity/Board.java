package com.knotted.entity;

import jakarta.persistence.*;
import lombok.Data;

// 게시판 엔티티

@Entity
@Table(name = "board")
@Data
public class Board extends BaseEntity{

    // 게시글 ID
    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    // 회원 엔티티와 다대일로 매핑
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    // 게시글 제목
    @Column(name = "board_subject")
    private String subject;

    // 게시글 내용
    @Column(name = "board_content")
    private String content;


}






