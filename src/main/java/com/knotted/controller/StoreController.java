package com.knotted.controller;

import com.knotted.dto.StoreDTO;
import com.knotted.dto.StoreFormDTO;
import com.knotted.service.FavoriteService;
import com.knotted.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequestMapping("/store")
@Controller
@RequiredArgsConstructor
@Slf4j
public class StoreController {

    private final StoreService storeService;
    private final FavoriteService favoriteService;

    // 매장 메인 페이지. 매장 리스트도 뿌려준다.
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        List<StoreDTO> storeList = storeService.getAllStores();
        model.addAttribute("storeList", storeList);

        return "store/index";
    }

    // 검색어로 매장 리스트 반환 (REST)
    @PostMapping(value = "/search")
    @ResponseBody
    public ResponseEntity<List<StoreDTO>> getStoresBySearch(@RequestParam("searchWord") String searchWord, Principal principal){
        try{
            List<StoreDTO> storeList = storeService.getStoresBySearchWord(searchWord, principal);
            return new ResponseEntity<>(storeList, HttpStatus.OK);
        }catch(Exception e){
            log.info(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 매장 상세 페이지
    @GetMapping(value = "/{storeId}")
    public String storeDetail(@PathVariable("storeId") Long storeId, Model model, Principal principal){
        try {
            StoreFormDTO storeFormDTO = storeService.getStore(storeId);
            model.addAttribute("storeFormDTO", storeFormDTO);
            
            // 해당 사용자가 즐겨찾기 갖고 있는지 확인
            if(principal != null){ // 로그인한 사용자의 경우에만 추가
                String memberEmail = principal.getName();

                boolean favoriteExists = favoriteService.storeFavoriteExists(memberEmail, storeId);
                model.addAttribute("favoriteExists", favoriteExists);
            }
            
        } catch (Exception e) {
            model.addAttribute("errorMessage", "존재하지 않는 매장입니다");
            return "redirect:/store";
        }

        return "store/storeDetail";
    }

}
