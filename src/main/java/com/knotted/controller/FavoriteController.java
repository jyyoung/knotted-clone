package com.knotted.controller;


import com.knotted.service.FavoriteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@RequestMapping("/favorite")
@Controller
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    // 매장 즐겨찾기 토글
    @PostMapping("/store/{storeId}")
    @ResponseBody
    public ResponseEntity<Boolean> toggleStoreFavorite(@PathVariable("storeId") Long storeId, Principal principal){
        // 현재 실행의 결과로 추가한 거면 true 반환, 삭제한 거면 false 반환

        String memberEmail = principal.getName();

        try {
            boolean toggle = favoriteService.toggleStoreFavorite(storeId, memberEmail);

            // toggle이 true이면 방금 추가한 것, false이면 방금 삭제한 것

            return new ResponseEntity<>(toggle, HttpStatus.OK);

        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 상품 즐겨찾기 토글
    @PostMapping("/item/{itemId}")
    @ResponseBody
    public ResponseEntity<Boolean> toggleItemFavorite(@PathVariable("itemId") Long itemId, Principal principal){
        // 현재 실행의 결과로 추가한 거면 true 반환, 삭제한 거면 false 반환

        String memberEmail = principal.getName();

        try {
            boolean toggle = favoriteService.toggleItemFavorite(itemId, memberEmail);

            // toggle이 true이면 방금 추가한 것, false이면 방금 삭제한 것

            return new ResponseEntity<>(toggle, HttpStatus.OK);

        } catch (EntityNotFoundException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}








