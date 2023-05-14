package com.knotted.controller;

import com.knotted.dto.ItemDTO;
import com.knotted.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/menu")
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 상품 메인 페이지. 상품 리스트도 뿌려준다.
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        List<ItemDTO> itemList = itemService.getAllItem();
        model.addAttribute("itemList", itemList);

        return "/item/index";
    }
}
