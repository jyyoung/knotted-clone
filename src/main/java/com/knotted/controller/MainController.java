package com.knotted.controller;

import com.knotted.dto.BoardDTO;
import com.knotted.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BoardService boardService;

    // 메인 페이지로 이동. 베스트 메뉴, 이벤트도 같이 넘김
    @GetMapping(value = "/")
    public String main(Model model){
        List<BoardDTO> boardDTOList = boardService.getCategoryBoards("event");

        model.addAttribute("boardDTOList", boardDTOList);

        return "index";
    }
}
