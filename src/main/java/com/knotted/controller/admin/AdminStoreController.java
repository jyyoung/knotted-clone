package com.knotted.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/store")
@Controller
@RequiredArgsConstructor
public class AdminStoreController {
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        return "/admin/store/index";
    }

    @GetMapping(value = "/new")
    public String main(){
        return "/admin/store/storeForm";
    }

}
