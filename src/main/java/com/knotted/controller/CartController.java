package com.knotted.controller;

import com.knotted.dto.CartItemDTO;
import com.knotted.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RequestMapping("/cart")
@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 메인 및 리스트 조회
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        List<CartItemDTO> cartItemList = new ArrayList<>();
        model.addAttribute("cartItemList", cartItemList);

        return "/cart/index";
    }

    // 장바구니에 상품을 추가한다
    // 주문 시 현재 장바구니가 있고, 해당 장바구니에 담긴 매장이 새로 들어온 주문과 같으면 현재 장바구니 아이템에 추가만 하고, 시간은 현재 고른 걸로 업데이트한다.
    // 만약 현재 장바구니에 담긴 매장과 다르면 장바구니 삭제 후 다시 추가하는 것으로 한다.
    @PostMapping(value = "/add")
    @ResponseBody
    public ResponseEntity<String> addToCart(@RequestParam("storeId") Long storeId, @RequestParam("itemId") Long itemId, @RequestParam("bookDate") String bookDate, @RequestParam("bookTime") String bookTime, @RequestParam("count") Long count){

        return new ResponseEntity<>("테스트", HttpStatus.OK);
    }

    // 장바구니에서 상품을 제거함
    @PostMapping(value = "/remove")
    @ResponseBody
    public ResponseEntity<String> removeFromCart(@RequestParam("cartId") Long cartId, @RequestParam("cartItemId") Long cartItemId){

        return new ResponseEntity<>("테스트", HttpStatus.OK);

    }
    
}
