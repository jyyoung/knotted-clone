package com.knotted.controller.admin;

import com.knotted.dto.BoardFormDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/board")
@Controller
@RequiredArgsConstructor
public class AdminBoardController {

    // 게시판 관리 메인. 목록도 읽어옴
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        return "/admin/board/index";
    }

    // 게시글 등록 페이지로 이동
    @GetMapping(value = "/new")
    public String boardForm(Model model){
        model.addAttribute("boardFormDTO", new BoardFormDTO());

        return "/admin/board/boardForm";
    }

}
