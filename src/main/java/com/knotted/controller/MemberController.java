package com.knotted.controller;

import com.knotted.dto.MemberDTO;
import com.knotted.dto.MemberFormDTO;
import com.knotted.entity.Member;
import com.knotted.repository.MemberRepository;
import com.knotted.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequestMapping("/member")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder; // 암호화용

    // 로그인 페이지로 이동
    @GetMapping(value = "/login")
    public String loginForm(){
        return "/member/loginForm";
    }

    // 로그인 실패 페이지로 이동
    @GetMapping(value = "/login/error")
    public String loginError(@RequestParam(name = "error", required = false) String error, Model model){
        model.addAttribute("errorMessage", "이메일 또는 비밀번호를 확인해주세요");

        return "/member/loginForm";
    }

    // 회원가입 페이지로 이동
    @GetMapping(value = "/join")
    public String joinForm(Model model){
        model.addAttribute("memberFormDTO", new MemberFormDTO());

        return "/member/joinForm";
    }

    // 회원가입 처리
    @PostMapping(value = "/join")
    public String joinSubmit(@Valid MemberFormDTO memberFormDTO, BindingResult bindingResult, Model model){

        // DTO의 Validation이 실패했을 경우 이동
        if(bindingResult.hasErrors()){
            return "/member/joinForm";
        }

        Member member = Member.createMember(memberFormDTO, passwordEncoder);
        memberService.saveMember(member);

        return "redirect:/member/complete?mode=join&email=" + member.getEmail();
    }

    // 이메일 중복 확인
    @PostMapping(value = "/emailCheck")
    @ResponseBody
    public ResponseEntity emailCheck(String email){
        // 성공, 실패 여부를 true, false라는 문자열로 보내기
        String result = "false";

        // 이메일 중복 검사에 성공 시 true 반환
        if(memberService.checkDuplicateEmail(email)){
            result = "true";
        }

        return new ResponseEntity<String>(result, HttpStatus.OK);
    }

    // 로그인, 회원가입, 회원탈퇴 성공 페이지로 이동
    @GetMapping(value = "/complete")
    public String complete(@RequestParam(name = "mode") String mode, @RequestParam(name = "email", required = false) String email, Model model){
        model.addAttribute("mode", mode);
        model.addAttribute("email", email);
        return "/member/complete";
    }

    // 회원정보 수정 비밀번호 확인 페이지로 이동
    @GetMapping(value = "/before-modify")
    public String beforeModify(Model model, Principal principal){
        String memberEmail = principal.getName();
        Member member = memberRepository.findByEmail(memberEmail);

        MemberDTO memberDTO = MemberDTO.of(member);
        model.addAttribute("memberDTO", memberDTO);

        return "/member/beforeModify";
    }

    // 회원정보 수정 비밀번호 확인 처리
    @PostMapping(value = "/before-modify")
    @ResponseBody
    public ResponseEntity<Void> beforeModifySubmit(@RequestParam("password") String password, Principal principal){
        String memberEmail = principal.getName();
        Member member = memberRepository.findByEmail(memberEmail);

        String encodedPassword = member.getPassword(); // 저장된 인코딩된 비밀번호
        String rawPassword = password; // 입력 폼으로 넘어온 비밀번호

        if(verifyPassword(rawPassword, encodedPassword)){ // 비밀번호 일치 시
            return new ResponseEntity<>(HttpStatus.OK);
        }else{ // 비밀번호 틀렸을 시
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // 회원정보 수정 페이지로 이동
    @GetMapping(value = "/modify")
    public String modifyForm(Model model, Principal principal){
        String memberEmail = principal.getName();
        Member member = memberRepository.findByEmail(memberEmail);

        MemberFormDTO memberFormDTO = MemberFormDTO.of(member);
        model.addAttribute("memberFormDTO", memberFormDTO);

        return "/member/modifyForm";
    }
    
    // PasswordEncoder 객체의 matches() 메소드로 인코딩되지 않은 비밀번호와 인코딩된 비밀번호가 같은지 여부 확인
    private boolean verifyPassword(String rawPassword, String encodedPassword){
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // 회원정보 수정 처리
    @PostMapping(value = "/modify")
    public String modifySubmit(@Valid MemberFormDTO memberFormDTO, BindingResult bindingResult, Model model, Principal principal){

        String memberEmail = principal.getName();
        Member member = memberRepository.findByEmail(memberEmail);

        String encodedPassword = member.getPassword(); // 저장된 인코딩된 비밀번호
        String rawPassword = memberFormDTO.getNowPassword(); // 폼으로 넘어온 현재 비밀번호

        if(!verifyPassword(rawPassword, encodedPassword)){ // 현재 비밀번호 불일치 시
            // nowPassword 필드에 대한 에러를 추가한다
            bindingResult.addError(new FieldError("memberFormDTO", "nowPassword", "현재 비밀번호가 일치하지 않습니다"));
        }

        if(bindingResult.hasErrors()){
            return "/member/modifyForm";
        }

        // 회원정보 수정 메소드 호출
        // 컨트롤러에서 이렇게 트랜잭션 없이 업데이트하면 안 된다. 서비스단에서 구현하든지 해야 한다.
        memberService.updateMember(member, memberFormDTO, passwordEncoder);

        // 수정 성공하면 mypage로 넘어감
        return "redirect:/mypage";
    }

    // 회원 탈퇴 페이지로 이동
    @GetMapping(value = "/withdraw")
    public String withdrawForm(Model model, Principal principal){

        String memberEmail = principal.getName();
        Member member = memberRepository.findByEmail(memberEmail);

        MemberDTO memberDTO = MemberDTO.of(member);
        model.addAttribute("memberDTO", memberDTO);

        return "/member/withdrawForm";
    }

    // 회원 탈퇴 처리 (REST로 처리)
    @PostMapping(value = "/withdraw")
    @ResponseBody
    public ResponseEntity<Void> withdrawSubmit(@RequestParam("password") String password, @RequestParam("reason") String reason, Principal principal){

        String memberEmail = principal.getName();
        Member member = memberRepository.findByEmail(memberEmail);

        String encodedPassword = member.getPassword(); // 저장된 인코딩된 비밀번호
        String rawPassword = password; // 입력 폼으로 넘어온 비밀번호

        if(!verifyPassword(rawPassword, encodedPassword)){ // 비밀번호 불일치 시
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else{
            // 회원 탈퇴 처리하기
            memberService.withdrawMember(member, reason);
            
            return new ResponseEntity<>(HttpStatus.OK);
        }

    }

}





