package com.knotted.controller;

import com.knotted.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping(value = "/{category}")
    public String showBoard(@PathVariable String category, Model model){
        String categoryName = boardService.getBoardCategory(category);
        if(!categoryName.equals("")){ // 존재하면
            model.addAttribute("category", categoryName);

            return "/board/index";
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
