package com.knotted.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/menu")
@Controller
@RequiredArgsConstructor
public class ItemController {
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        return "/item/index";
    }
}
