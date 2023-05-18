package com.knotted.controller;

import com.knotted.dto.StoreDTO;
import com.knotted.dto.StoreFormDTO;
import com.knotted.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/store")
@Controller
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 매장 메인 페이지. 매장 리스트도 뿌려준다.
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        List<StoreDTO> storeList = storeService.getAllStores();
        model.addAttribute("storeList", storeList);

        return "/store/index";
    }

    // 검색어로 매장 리스트 반환 (REST)
    @PostMapping(value = "/search")
    @ResponseBody
    public ResponseEntity<List<StoreDTO>> getStoresBySearch(@RequestParam("searchWord") String searchWord){
        try{
            List<StoreDTO> storeList = storeService.getStoresBySearchWord(searchWord);
            return new ResponseEntity<>(storeList, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 매장 상세 페이지
    @GetMapping(value = "/{storeId}")
    public String storeDetail(@PathVariable("storeId") Long storeId, Model model){
        try {
            StoreFormDTO storeFormDTO = storeService.getStore(storeId);
            model.addAttribute("storeFormDTO", storeFormDTO);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "존재하지 않는 매장입니다");
            return "redirect:/store";
        }

        return "/store/storeDetail";
    }

}
