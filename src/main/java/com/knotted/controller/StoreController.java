package com.knotted.controller;

import com.knotted.dto.StoreDTO;
import com.knotted.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/store")
@Controller
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 매장 메인 페이지. 매장 리스트도 뿌려준다.
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        List<StoreDTO> storeList = storeService.getAllStore();
        model.addAttribute("storeList", storeList);

        return "/store/index";
    }
}
