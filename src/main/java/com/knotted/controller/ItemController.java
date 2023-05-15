package com.knotted.controller;

import com.knotted.dto.ItemDTO;
import com.knotted.dto.ItemFormDTO;
import com.knotted.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping(value = "/category/{category}")
    @ResponseBody
    public ResponseEntity<List<ItemDTO>> getItemsByCategory(@PathVariable(value = "category") String category){
        try{
            List<ItemDTO> itemList = itemService.getItemsByCategoryAndSearchWord(category, "");
            return new ResponseEntity<>(itemList, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/search")
    @ResponseBody
    public ResponseEntity<List<ItemDTO>> getItemsBySearchWord(@RequestParam("category") String category,
                                                          @RequestParam("searchWord") String searchWord){
        try{
            List<ItemDTO> itemList = itemService.getItemsByCategoryAndSearchWord(category, searchWord);
            return new ResponseEntity<>(itemList, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 상품 상세 페이지
    @GetMapping(value = "/{itemId}")
    public String itemDetail(@PathVariable("itemId") Long itemId, Model model){
        try {
            ItemFormDTO itemFormDTO = itemService.getItem(itemId);
            model.addAttribute("itemFormDTO", itemFormDTO);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다");
            return "redirect:/item";
        }

        return "/item/itemDetail";
    }
}
