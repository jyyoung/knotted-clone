package com.knotted.repository;

import com.knotted.entity.BoardCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCategoryRepository extends JpaRepository<BoardCategory, Long> {
    BoardCategory findByCategory(String category); // 카테고리명으로 컨트롤링
}
