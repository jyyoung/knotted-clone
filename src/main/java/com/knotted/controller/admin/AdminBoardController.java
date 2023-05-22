package com.knotted.controller.admin;

import com.knotted.dto.BoardFormDTO;
import com.knotted.service.admin.AdminBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/admin/board")
@Controller
@RequiredArgsConstructor
public class AdminBoardController {

    private final AdminBoardService adminBoardService;

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
    
    // 게시글 등록 처리
    @PostMapping(value = "/new")
    public String boardSubmit(@Valid BoardFormDTO boardFormDTO, BindingResult bindingResult, Model model, MultipartFile boardImageFile){

        if(bindingResult.hasErrors()){
            return "/admin/board/boardForm";
        }

        if(!boardImageFile.isEmpty() && !boardImageFile.getContentType().startsWith("image/")){ // 파일을 올렸는데 해당 파일이 이미지 파일이 아니라면
            model.addAttribute("errorMessage", "이미지 파일이 아닙니다");
            return "/admin/board/boardForm";
        }

        // 이미지가 있으면 게시글 및 게시글 이미지 저장 로직을 호출
        try{
            adminBoardService.saveBoard(boardFormDTO, boardImageFile);
        }catch (Exception e){
            model.addAttribute("errorMessage", e.getMessage());
            return "/admin/board/boardForm";
        }

        // 성공 시 게시글 관리 페이지로 이동
        return "redirect:/admin/board";
    }

}
