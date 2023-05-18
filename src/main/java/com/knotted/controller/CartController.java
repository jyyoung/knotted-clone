package com.knotted.controller;

import com.knotted.dto.CartDTO;
import com.knotted.service.CartService;
import com.knotted.util.TimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;

@RequestMapping("/cart")
@Controller
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 메인 및 리스트 조회
    @GetMapping(value = {"", "/"})
    public String main(Model model, Principal principal){
        String memberEmail = principal.getName();

        CartDTO cartDTO = cartService.getCart(memberEmail);

        if(cartDTO.getId() != null){
            String reserveDate = TimeUtils.localDateTimeToString(cartDTO.getReserveDate());
            model.addAttribute("cart", cartDTO);
            model.addAttribute("reserveDate", reserveDate);
        }

        return "/cart/index";
    }

    // 장바구니 리스트 조회
    @PostMapping(value = {"", "/"})
    @ResponseBody
    public ResponseEntity<CartDTO> getCartItems(Principal principal){
        try {
            String memberEmail = principal.getName();

            CartDTO cartDTO = cartService.getCart(memberEmail);

            return new ResponseEntity<>(cartDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 장바구니에 상품을 추가한다
    // 주문 시 현재 장바구니가 있고, 해당 장바구니에 담긴 매장이 새로 들어온 주문과 같으면 현재 장바구니 아이템에 추가만 하고, 시간은 현재 고른 걸로 업데이트한다.
    // 만약 현재 장바구니에 담긴 매장과 다르면 장바구니 삭제 후 다시 추가하는 것으로 한다.
    @PostMapping(value = "/add")
    @ResponseBody
    public ResponseEntity<String> addToCart(@RequestParam("storeId") Long storeId, @RequestParam("itemId") Long itemId, @RequestParam("bookDate") String bookDate, @RequestParam("bookTime") String bookTime, @RequestParam("count") Long count, Principal principal){

        // 장바구니를 생성
        // 장바구니 상품 생성 (일단 무조건 하나씩이니까. 그리고 이미 있으면 기존 장바구니 상품에 count 추가하고)
        // 일단 예약일시를 합치고, LocalDateTime으로 변환한다
        // 자주 쓰는 거라 TimeUtils 패키지에 메소드로 구현해놓았다.
        String reserveDateString = bookDate + " " + bookTime;
        LocalDateTime reserveDate = TimeUtils.stringToLocalDateTime(reserveDateString);

        // 장바구니와 장바구니 생성을 하나의 Service 메소드에서 한다 (그래야 트랜잭션이 되니까)
        try {
            String memberEmail = principal.getName();

            cartService.addToCart(memberEmail, storeId, itemId, reserveDate, count);
        } catch (Exception e) {
            return new ResponseEntity<>("장바구니 담기 중 에러가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>("success" + reserveDate, HttpStatus.OK);
    }

    // 장바구니에서 상품을 제거함
    @PostMapping(value = "/delete")
    @ResponseBody
    public ResponseEntity<String> deleteFromCart(@RequestParam("cartItemId") Long cartItemId, Principal principal){
        // 삭제하는 게 본인 것인지 확인해야 함
        try {
            String memberEmail = principal.getName();
            cartService.deleteCartItem(memberEmail, cartItemId);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 장바구니에 담기 전 기존 장바구니의 매장과 같은지 확인
    @GetMapping(value = "/storeCheck")
    @ResponseBody
    public ResponseEntity<Boolean> storeCheck(@RequestParam("storeId") Long storeId, Principal principal){
        try {
            String memberEmail = principal.getName(); // 현재 사용자의 이메일

            // 다른 매장의 상품이 장바구니에 있다면
            if(!cartService.storeCheck(memberEmail, storeId)){
                return new ResponseEntity<Boolean>(false, HttpStatus.OK);
            }else{
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 장바구니 전체 비우기
    @PostMapping(value = "/deleteAll")
    @ResponseBody
    public ResponseEntity<Void> deleteAll(Principal principal){
        try {
            String memberEmail = principal.getName();

            // 장바구니 엔티티 삭제 (장바구니 상품은 cascade하게 지워짐)
            cartService.deleteCart(memberEmail);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 장바구니 상품 개수 변경
    @PostMapping(value = "/changeCount")
    @ResponseBody
    public ResponseEntity<Void> changeCount(@RequestParam("cartItemId") Long cartItemId, @RequestParam("count") Long count, Principal principal){
        // 바꾸는 게 본인 것인지 확인해야 함

        try {
            String memberEmail = principal.getName();
            cartService.changeCount(memberEmail, cartItemId, count);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}






