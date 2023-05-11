package com.knotted.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/map")
@Controller
@RequiredArgsConstructor
public class MapController {
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        return "/map/index";
    }
}