package com.knotted.controller;

import com.knotted.dto.MemberFormDTO;
import com.knotted.entity.Member;
import com.knotted.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/member/*")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder; // 암호화용

    @GetMapping(value = "/join")
    public String joinForm(Model model){
        model.addAttribute("memberFormDTO", new MemberFormDTO());

        return "/member/join";
    }

    @PostMapping(value = "/join")
    public String joinSubmit(@Valid MemberFormDTO memberFormDTO, BindingResult bindingResult, Model model){

        // DTO의 Validation이 실패했을 경우 이동
        if(bindingResult.hasErrors()){
            return "/member/join";
        }

        try {
            Member member = Member.createMember(memberFormDTO, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage()); // 에러 메시지 전달
            return "/member/join";
        }

        return "redirect:/";
    }
}
