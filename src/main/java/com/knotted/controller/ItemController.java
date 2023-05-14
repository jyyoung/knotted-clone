package com.knotted.controller;

import com.knotted.dto.ItemDTO;
import com.knotted.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/menu")
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    // 상품 메인 페이지. 상품 리스트도 뿌려준다.
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        List<ItemDTO> itemList = itemService.getAllItems();
        model.addAttribute("itemList", itemList);

        return "/item/index";
    }

    // 카테고리 선택 시 해당 카테고리 리스트 반환 (REST)
    @GetMapping(value = {"/category/{category}"})
    @ResponseBody
    public ResponseEntity<List<ItemDTO>> getItemsByCategory(@PathVariable(value = "category") String category){
        try{
            List<ItemDTO> itemList = itemService.getItemsByCategory(category);
            return new ResponseEntity<>(itemList, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
