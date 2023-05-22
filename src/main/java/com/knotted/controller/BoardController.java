package com.knotted.controller;

import com.knotted.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 각 게시판 카테고리 메인으로 이동
    @GetMapping(value = "/{category}")
    public String boardList(@PathVariable("category") String category, Model model){

        // 카테고리에 따라 다른 View를 보여줄 것임
        return "/board/index";
    }

    // 각 카테고리 글 보기 (카테고리로 따로 검색하진 않음. 그냥 조건 분기용)
    @GetMapping(value = "/{category}/{boardId}")
    public String viewBoard(@PathVariable("category") String category, @PathVariable("boardId") Long boardId, Model model){

        // 카테고리에 따라 다른 View를 보여줄 것임
        return "/board/index";
    }
}
