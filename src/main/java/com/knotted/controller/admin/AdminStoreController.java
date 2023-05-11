package com.knotted.controller.admin;

import com.knotted.dto.StoreFormDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/admin/store")
@Controller
@RequiredArgsConstructor
public class AdminStoreController {
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        return "/admin/store/index";
    }

    // 매장 등록 페이지로 이동
    @GetMapping(value = "/new")
    public String storeForm(Model model){
        model.addAttribute("storeFormDTO", new StoreFormDTO());

        return "/admin/store/storeForm";
    }

    @PostMapping(value = "/new")
    public String storeSubmit(@Valid StoreFormDTO storeFormDTO, BindingResult bindingResult, Model model, MultipartFile storeImageFile){

        if(bindingResult.hasErrors()){
            return "/admin/store/storeForm";
        }

        if(storeImageFile.isEmpty()){ // 이미지가 없다면
            model.addAttribute("errorMessage", "매장 이미지가 없습니다");
            return "/admin/store/storeForm";
        }

        // 성공 시 매장 관리 페이지로 이동
        return "redirect:/admin/store";
    }

}
