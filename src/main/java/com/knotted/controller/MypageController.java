package com.knotted.controller;


import com.knotted.dto.MemberDTO;
import com.knotted.dto.RewardHistoryDTO;
import com.knotted.entity.Member;
import com.knotted.repository.MemberRepository;
import com.knotted.service.RewardHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@RequestMapping("/mypage")
@Controller
@RequiredArgsConstructor
public class MypageController {

    private final MemberRepository memberRepository;
    private final RewardHistoryService rewardHistoryService;
    
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
    public String myScore(Model model, Principal principal){
        String memberEmail = principal.getName();

        Member member = memberRepository.findByEmail(memberEmail);
        MemberDTO memberDTO = MemberDTO.of(member);

        model.addAttribute("memberDTO", memberDTO);

        return "/mypage/myScore";
    }

    // 적립 내역 페이지로 이동
    @GetMapping(value = "/point")
    public String point(Model model, Principal principal){
        String memberEmail = principal.getName();

        Member member = memberRepository.findByEmail(memberEmail);
        MemberDTO memberDTO = MemberDTO.of(member);

        // 해당 회원의 적립내역을 조회한다
        
        // 적립금 획득 내역, 사용 내역을 따로 담는 것이 좋을 듯
        List<RewardHistoryDTO> acquireList = rewardHistoryService.getAcquireRewardHistoryByMember(member);
        List<RewardHistoryDTO> useList = rewardHistoryService.getUseRewardHistoryByMember(member);

        model.addAttribute("memberDTO", memberDTO);
        model.addAttribute("acquireList", acquireList);
        model.addAttribute("useList", useList);

        return "/mypage/point";
    }

    // 찜 리스트 페이지로 이동
    @GetMapping(value = "/bookmark")
    public String bookmark(Model model, Principal principal){
        String memberEmail = principal.getName();

        Member member = memberRepository.findByEmail(memberEmail);
        MemberDTO memberDTO = MemberDTO.of(member);

        model.addAttribute("memberDTO", memberDTO);

        return "/mypage/bookmark";
    }
}
