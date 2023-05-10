package com.knotted.controller;

import com.knotted.dto.MemberFormDTO;
import com.knotted.entity.Member;
import com.knotted.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/member/*")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder; // 암호화용

    @GetMapping(value = "/login")
    public String loginForm(){
        return "/member/login";
    }

    // 로그인 실패 처리를 위해 매핑을 하나 추가함
    @GetMapping(value = "/login/error")
    public String loginError(@RequestParam(name = "error", required = false) String error, Model model){
        model.addAttribute("errorMessage", "이메일 또는 비밀번호를 확인해주세요");

        return "/member/login";
    }

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

        Member member = Member.createMember(memberFormDTO, passwordEncoder);
        memberService.saveMember(member);

        return "redirect:/";
    }

    @PostMapping(value = "/emailCheck")
    @ResponseBody
    public ResponseEntity emailCheck(String email){
        // 성공, 실패 여부를 true, false라는 문자열로 보내기
        String result = "";

        return new ResponseEntity<String>(result, HttpStatus.OK);
    }
}
