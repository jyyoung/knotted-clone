package com.knotted.controller;

import com.knotted.dto.ItemDTO;
import com.knotted.dto.ItemFormDTO;
import com.knotted.entity.StoreItem;
import com.knotted.repository.StoreItemRepository;
import com.knotted.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/menu")
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final StoreItemRepository storeItemRepository; // 해당 매장 상품의 개수를 파악하기 위해 추가함

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

    // 예약 상품 고르는 부분에서 재고 확인을 위해 추가함
    @PostMapping(value = "/search/storeItem")
    @ResponseBody
    public ResponseEntity<List<ItemDTO>> getItemsByStoreItem(@RequestParam("category") String category,
                                                             @RequestParam("searchWord") String searchWord, @RequestParam("storeId") Long storeId){
        try{
            List<ItemDTO> itemList = itemService.getItemsByCategoryAndSearchWord(category, searchWord);
            List<ItemDTO> newItemList = new ArrayList<>(); // 재고 여부를 추가해 담을 리스트

            // 특정 매장의 상품 재고 여부 확인을 위해 추가함
            // storeId가 있을 때만 처리
            if(storeId != 0){
                for(ItemDTO itemDTO : itemList){
                    StoreItem storeItem = storeItemRepository.findByStoreIdAndItemId(storeId, itemDTO.getId());
                    if(storeItem == null || storeItem.getStock() == 0){
                        itemDTO.setOnStock(false);
                    }else{
                        itemDTO.setOnStock(true);
                    }

                    newItemList.add(itemDTO);
                }
            }

            return new ResponseEntity<>(newItemList, HttpStatus.OK);

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
