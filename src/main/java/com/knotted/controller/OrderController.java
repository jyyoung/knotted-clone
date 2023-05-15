package com.knotted.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/order")
@Controller
@RequiredArgsConstructor
public class OrderController {

    // 주문(예약) 메인으로 이동
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        return "/order/index";
    }

    // 예약 - 매장선택 페이지로 이동
    @GetMapping(value = "/store-pick")
    public String storePick(){
        return "/order/storePick";
    }


}
