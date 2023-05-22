package com.knotted.repository.admin;

import com.knotted.entity.BoardImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminBoardImageRepository extends JpaRepository<BoardImage, Long> {

}
