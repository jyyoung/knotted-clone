package com.knotted.controller.admin;

import com.knotted.dto.ItemDTO;
import com.knotted.dto.ItemFormDTO;
import com.knotted.dto.ItemImageDTO;
import com.knotted.service.admin.AdminItemService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/admin/item")
@Controller
@RequiredArgsConstructor
public class AdminItemController {

    private final AdminItemService adminItemService;
    
    // 상품 관리 메인 페이지. 상품 리스트도 뿌려준다.
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        List<ItemDTO> itemList = adminItemService.getAllItems();
        model.addAttribute("itemList", itemList);

        return "/admin/item/index";
    }

    // 상품 등록 페이지로 이동
    @GetMapping(value = "/new")
    public String itemForm(Model model){
        model.addAttribute("itemFormDTO", new ItemFormDTO());

        return "/admin/item/itemForm";
    }

    // 상품 등록 처리
    @PostMapping(value = "/new")
    public String itemSubmit(@Valid ItemFormDTO itemFormDTO, BindingResult bindingResult, Model model, MultipartFile itemImageFile){

        if(bindingResult.hasErrors()){
            return "/admin/item/itemForm";
        }

        if(itemImageFile.isEmpty()){ // 이미지가 없다면
            model.addAttribute("errorMessage", "상품 이미지가 없습니다");
            return "/admin/item/itemForm";
        }

        if(!itemImageFile.getContentType().startsWith("image/")){ // 이미지 파일이 아니라면
            model.addAttribute("errorMessage", "이미지 파일이 아닙니다");
            return "/admin/item/itemForm";
        }

        // 이미지가 있으면 상품 및 상품 이미지 저장 로직을 호출
        try{
            adminItemService.saveItem(itemFormDTO, itemImageFile);
        }catch (Exception e){
            model.addAttribute("errorMessage", "상품 등록 중 에러가 발생했습니다");
            return "/admin/item/itemForm";
        }

        // 성공 시 상품 관리 페이지로 이동
        return "redirect:/admin/item";
    }

    // 상품 삭제 처리
    @DeleteMapping(value = "/{itemId}")
    @ResponseBody
    public ResponseEntity<Void> deleteItem(@PathVariable("itemId") Long itemId){

        try {
            adminItemService.deleteItem(itemId);
        } catch (Exception e) {
            return new ResponseEntity("상품 삭제 중 에러가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity.noContent().build();
    }

    // 상품 수정 페이지로 이동
    @GetMapping(value = "/{itemId}")
    public String itemFormUpdate(@PathVariable("itemId") Long itemId, Model model){
        // ItemFormDTO를 넘겨준다

        try {
            ItemFormDTO itemFormDTO = adminItemService.getItem(itemId);
            model.addAttribute("itemId", itemId);
            model.addAttribute("itemFormDTO", itemFormDTO);
        } catch (EntityNotFoundException e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다");
            return "redirect:/admin/item";
        }

        return "/admin/item/itemForm";
    }


    // 상품 수정 처리
    @PostMapping(value = "/{itemId}")
    public String itemUpdate(@PathVariable("itemId") Long itemId, String imageName, String imageId, @Valid ItemFormDTO itemFormDTO, BindingResult bindingResult, Model model, MultipartFile itemImageFile){
        
        if(bindingResult.hasErrors()){
            // 바인딩 에러 나면 itemImageDTO가 없어서 타임리프에서 NPE가 뜨는 것 방지
            ItemImageDTO itemImageDTO = new ItemImageDTO();
            itemImageDTO.setOriginalImageName(imageName);
            itemImageDTO.setId(Long.valueOf(imageId));
            itemFormDTO.setItemImageDTO(itemImageDTO);

            return "/admin/item/itemForm";
        }

        // 이미지 관련 작업부터 하기
        if(!itemImageFile.isEmpty()) { // 새로 올린 파일이 있다면
            // 이미지인지 확인
            if(!itemImageFile.getContentType().startsWith("image/")){ // 이미지 파일이 아니라면
                model.addAttribute("errorMessage", "이미지 파일이 아닙니다");
                return "/admin/item/itemForm";
            }
        }

        try {
            adminItemService.updateItem(itemFormDTO, itemImageFile);
        } catch(Exception e) {
            model.addAttribute("errorMessage", "상품 수정 중 에러가 발생했습니다");
            return "/admin/item/itemForm";
        }

        // 성공 시 상품 관리 페이지로 이동
        return "redirect:/admin/item";
    }
}
