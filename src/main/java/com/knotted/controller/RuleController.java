package com.knotted.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

@RequestMapping("/rule")
@Controller
@RequiredArgsConstructor
public class RuleController {

    // web, privacy, location만 허용
    @GetMapping(value = "/{rule}")
    public String showRule(@PathVariable String rule, Model model){
        String[] rules = {"web", "privacy", "location"};

        if(Arrays.asList(rules).contains(rule)){ // 위의 rules 안에 있는 경우
            model.addAttribute("rule", rule);
            return "/rule/index";
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }

}
