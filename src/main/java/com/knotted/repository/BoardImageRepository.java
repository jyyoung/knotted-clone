package com.knotted.repository;

import com.knotted.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {

    // 게시글 ID로 해당 이미지 조회
    BoardImage findByBoardId(Long boardId);
}
