package com.knotted.controller.admin;

import com.knotted.dto.BoardDTO;
import com.knotted.dto.BoardFormDTO;
import com.knotted.dto.MemberDTO;
import com.knotted.entity.Member;
import com.knotted.repository.MemberRepository;
import com.knotted.service.BoardService;
import com.knotted.service.admin.AdminBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RequestMapping("/admin/board")
@Controller
@RequiredArgsConstructor
public class AdminBoardController {

    private final BoardService boardService;
    private final AdminBoardService adminBoardService;
    private final MemberRepository memberRepository;

    // 게시판 관리 메인. 목록도 읽어옴
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        List<BoardDTO> boardDTOList = boardService.getAllBoards();

        model.addAttribute("boardDTOList", boardDTOList);

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
    public String boardSubmit(@Valid BoardFormDTO boardFormDTO, BindingResult bindingResult, Model model, MultipartFile boardImageFile, Principal principal){

        if(bindingResult.hasErrors()){
            return "/admin/board/boardForm";
        }

        Member member = memberRepository.findByEmail(principal.getName());
        MemberDTO memberDTO = MemberDTO.of(member);
        boardFormDTO.setMemberDTO(memberDTO);

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

    // 게시글 삭제 처리
    @DeleteMapping(value = "/{boardId}")
    @ResponseBody
    public ResponseEntity<Void> deleteBoard(@PathVariable("boardId") Long boardId){

        try {
            adminBoardService.deleteBoard(boardId);
        } catch (Exception e) {
            return new ResponseEntity("게시글 삭제 중 에러가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.noContent().build();
    }


}
