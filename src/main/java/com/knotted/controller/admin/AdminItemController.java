package com.knotted.controller.admin;

import com.knotted.dto.ItemFormDTO;
import com.knotted.service.admin.AdminItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin/item")
@Controller
@RequiredArgsConstructor
public class AdminItemController {

    private final AdminItemService adminItemService;
    
    // 상품 관리 메인 페이지. 상품 리스트도 뿌려준다.
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        return "/admin/item/index";
    }

    // 상품 등록 페이지로 이동
    @GetMapping(value = "/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDTO", new ItemFormDTO());

        return "/admin/item/itemForm";
    }

}
