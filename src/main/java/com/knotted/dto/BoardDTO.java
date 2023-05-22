package com.knotted.dto;

import com.knotted.entity.Board;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class BoardDTO {

    // 게시글 ID
    private Long id;

    // 회원 DTO
    private MemberDTO memberDTO;

    // 게시글 카테고리
    private String category;

    // 게시글 제목
    private String subject;

    // 게시글 내용
    private String content;

    // 게시글 이미지
    private BoardImageDTO boardImageDTO;

    // 엔티티 <-> DTO간 변환에 사용할 ModelMapper 객체
    private static ModelMapper modelMapper = new ModelMapper();

    // 엔티티를 DTO로 변환하여 리턴
    public static BoardDTO of(Board board){
        return modelMapper.map(board, BoardDTO.class);
    }

}
