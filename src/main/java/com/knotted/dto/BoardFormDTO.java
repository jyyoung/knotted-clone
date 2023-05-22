package com.knotted.dto;


import com.knotted.entity.Board;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.modelmapper.ModelMapper;

@Data
public class BoardFormDTO {

    // 게시글 ID
    private Long id;

    // 게시글 카테고리
    @NotBlank(message = "카테고리는 필수 입력값입니다")
    @Pattern(regexp = "^(notice|faq|qna|event)$", message = "카테고리는 공지사항, 자주묻는질문, 1:1문의, 이벤트 중 하나여야 합니다")
    private String category;

    // 게시글 제목
    @NotBlank(message = "제목은 필수 입력값입니다")
    private String subject;

    // 게시글 내용
    @NotBlank(message = "내용은 필수 입력값입니다")
    private String content;

    // 게시글 이미지
    private BoardImageDTO boardImageDTO;

    // 엔티티 <-> DTO간 변환에 사용할 ModelMapper 객체
    private static ModelMapper modelMapper = new ModelMapper();

    // DTO -> 엔티티 변환
    public Board createBoard(){
        return modelMapper.map(this, Board.class);
    }

    // 엔티티 -> DTO 변환
    public static BoardFormDTO of(Board board){
        return modelMapper.map(board, BoardFormDTO.class);
    }
}
