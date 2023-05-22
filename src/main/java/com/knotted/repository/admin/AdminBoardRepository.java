package com.knotted.repository.admin;

import com.knotted.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminBoardRepository extends JpaRepository<Board, Long> {

}
