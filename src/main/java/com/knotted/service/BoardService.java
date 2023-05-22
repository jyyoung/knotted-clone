package com.knotted.service;

import com.knotted.dto.BoardDTO;
import com.knotted.dto.BoardFormDTO;
import com.knotted.dto.BoardImageDTO;
import com.knotted.entity.Board;
import com.knotted.entity.BoardImage;
import com.knotted.repository.BoardImageRepository;
import com.knotted.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardImageRepository boardImageRepository;

    // 모든 게시글 조회하는 메소드
    public List<BoardDTO> getAllBoards(){
        List<Board> boardList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<BoardDTO> boardDTOList = new ArrayList<>();

        for(Board board : boardList){
            boardDTOList.add(convertToBoardDTO(board));
        }

        return boardDTOList;
    }

    // 특정 게시판의 게시글만 조회하는 메소드
    public List<BoardDTO> getCategoryBoards(String category){
        List<Board> boardList = boardRepository.findByCategory(category, Sort.by(Sort.Direction.DESC, "id"));
        List<BoardDTO> boardDTOList = new ArrayList<>();

        for(Board board : boardList){
            boardDTOList.add(convertToBoardDTO(board));
        }

        return boardDTOList;
    }

    // 게시글 하나 조회하는 메소드
    public BoardFormDTO getBoard(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        BoardFormDTO boardFormDTO = BoardFormDTO.of(board);
        BoardImage boardImage = boardImageRepository.findByBoardId(board.getId());

        // 여기서 boardImage가 있는 경우에만 넣도록 함
        if(boardImage != null){
            BoardImageDTO boardImageDTO = BoardImageDTO.of(boardImage);
            boardFormDTO.setBoardImageDTO(boardImageDTO);
        }

        return boardFormDTO;
    }

    // 게시글 엔티티를 DTO로 변환 (이 과정에서 게시글 이미지 DTO도 게시글 DTO에 넣음)
    public BoardDTO convertToBoardDTO(Board board){
        BoardDTO boardDTO = BoardDTO.of(board);
        // 해당 Board로 BoardImage를 찾아내서 추가한다
        BoardImage boardImage = boardImageRepository.findByBoardId(board.getId());

        if(boardImage != null){ // 해당 이미지가 있으면
            BoardImageDTO boardImageDTO = BoardImageDTO.of(boardImage);
            boardDTO.setBoardImageDTO(boardImageDTO);
        }

        return boardDTO;
    }
}
