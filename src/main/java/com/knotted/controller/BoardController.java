package com.knotted.controller;

import com.knotted.dto.BoardFormDTO;
import com.knotted.entity.Member;
import com.knotted.repository.MemberRepository;
import com.knotted.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Arrays;

@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final MemberRepository memberRepository;

    // 각 게시판 카테고리 메인으로 이동
    @GetMapping(value = "/{category}")
    public String boardList(@PathVariable("category") String category, Model model){

        // 카테고리가 넷 중 하나가 아니면 메인으로 리다이렉트
        String[] categories = {"notice", "faq", "qna", "event"};
        if(!Arrays.asList(categories).contains(category)){ // 해당 리스트에 없는 경우
            return "redirect:/";
        }

        // 카테고리에 따라 다른 View를 보여줄 것임
        return "/board/index";
    }

    // 각 카테고리 글 보기 (카테고리로 따로 검색하진 않음. 그냥 조건 분기용)
    @GetMapping(value = "/{category}/{boardId}")
    public String viewBoard(@PathVariable("category") String category, @PathVariable("boardId") Long boardId, Model model, Principal principal){

        // 카테고리가 넷 중 하나가 아니면 메인으로 리다이렉트
        String[] categories = {"notice", "faq", "qna", "event"};
        if(!Arrays.asList(categories).contains(category)){ // 해당 리스트에 없는 경우
            return "redirect:/";
        }

        // 관리자인지 확인
        Member member = null;

        if(principal != null){
            member = memberRepository.findByEmail(principal.getName());
        }

        if(member != null && member.getMemberRole().toString().equals("ADMIN")){
            model.addAttribute("isAdmin", true);
        }else{
            model.addAttribute("isAdmin", false);
        }

        BoardFormDTO boardFormDTO = boardService.getBoard(boardId);
        model.addAttribute("boardId", boardId);
        model.addAttribute("boardFormDTO", boardFormDTO);

        // 카테고리에 따라 다른 View를 보여줄 것임
        return "/board/boardDetail";
    }
}
