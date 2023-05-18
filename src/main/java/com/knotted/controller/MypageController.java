package com.knotted.controller;


import com.knotted.dto.MemberDTO;
import com.knotted.entity.Member;
import com.knotted.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@RequestMapping("/mypage")
@Controller
@RequiredArgsConstructor
public class MypageController {

    private final MemberRepository memberRepository;
    
    // 마이페이지 메인으로 이동
    @GetMapping(value = {"", "/"})
    public String main(Model model, Principal principal){
        String memberEmail = principal.getName();

        Member member = memberRepository.findByEmail(memberEmail);
        MemberDTO memberDTO = MemberDTO.of(member);

        model.addAttribute("memberDTO", memberDTO);

        return "/mypage/index";
    }

    // 나의 등급 페이지로 이동
    @GetMapping(value = "/myscore")
    public String myScore(Model model){
        return "/mypage/myScore";
    }

    // 찜 리스트 페이지로 이동
    @GetMapping(value = "/bookmark")
    public String bookmark(Model model){
        return "/mypage/bookmark";
    }
}
