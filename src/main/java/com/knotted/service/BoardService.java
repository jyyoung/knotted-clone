package com.knotted.service;

import com.knotted.entity.BoardCategory;
import com.knotted.repository.BoardCategoryRepository;
import com.knotted.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final BoardCategoryRepository boardCategoryRepository;

    // 카테고리명 얻어오는 메소드
    public String getBoardCategory(String category){
        BoardCategory boardCategory = boardCategoryRepository.findByCategory(category);

        if(boardCategory != null){ // 해당 카테고리가 존재하면
            return boardCategory.getName();
        }else{
            return "";
        }
    }
}
