package com.knotted.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// 전역 커스텀 예외 처리

@ControllerAdvice // View를 반환하는 일반적인 컨트롤러에서 발생할 수 있는 예외를 처리
public class GlobalExceptionHandler {

    // 회원가입 시 이메일 중복인 경우
    @ExceptionHandler(DuplicateMemberException.class)
    public String handleDuplicateMemberException(DuplicateMemberException e, Model model){
        model.addAttribute("errorMessage", e.getMessage());
        return "/member/join";
    }
}
