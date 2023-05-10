package com.knotted.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
@RequiredArgsConstructor
public class AdminMainController { // 빈으로 등록된 클래스의 이름이 같으면 안 됨

    @GetMapping(value = {"", "/"})
    public String main(Model model){
        return "/admin/main";
    }

}
