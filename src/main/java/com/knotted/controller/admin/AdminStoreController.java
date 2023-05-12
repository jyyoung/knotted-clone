package com.knotted.controller.admin;

import com.knotted.dto.StoreDTO;
import com.knotted.dto.StoreFormDTO;
import com.knotted.service.admin.AdminStoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/admin/store")
@Controller
@RequiredArgsConstructor
public class AdminStoreController {

    private final AdminStoreService adminStoreService;

    // 매장 관리 메인 페이지. 매장 리스트도 뿌려준다.
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        List<StoreDTO> storeList = adminStoreService.getAllStore();
        model.addAttribute("storeList", storeList);

        return "/admin/store/index";
    }

    // 매장 등록 페이지로 이동
    @GetMapping(value = "/new")
    public String storeForm(Model model){
        model.addAttribute("storeFormDTO", new StoreFormDTO());

        return "/admin/store/storeForm";
    }

    // 매장 등록 처리
    @PostMapping(value = "/new")
    public String storeSubmit(@Valid StoreFormDTO storeFormDTO, BindingResult bindingResult, Model model, MultipartFile storeImageFile){

        if(bindingResult.hasErrors()){
            return "/admin/store/storeForm";
        }

        if(storeImageFile.isEmpty()){ // 이미지가 없다면
            model.addAttribute("errorMessage", "매장 이미지가 없습니다");
            return "/admin/store/storeForm";
        }
        
        if(!storeImageFile.getContentType().startsWith("image/")){ // 이미지 파일이 아니라면
            model.addAttribute("errorMessage", "이미지 파일이 아닙니다");
            return "/admin/store/storeForm";
        }

        // 이미지가 있으면 매장 및 매장 이미지 저장 로직을 호출
        try{
            adminStoreService.saveStore(storeFormDTO, storeImageFile);
        }catch (Exception e){
            model.addAttribute("errorMessage", "매장 등록 중 에러가 발생했습니다");
            return "/admin/store/storeForm";
        }

        // 성공 시 매장 관리 페이지로 이동
        return "redirect:/admin/store";
    }
}
