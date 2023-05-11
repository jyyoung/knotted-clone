package com.knotted.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/order")
@Controller
@RequiredArgsConstructor
public class AdminOrderController {
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        return "/admin/order/index";
    }
}
