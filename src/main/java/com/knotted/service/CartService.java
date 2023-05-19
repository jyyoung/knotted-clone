package com.knotted.service;

import com.knotted.dto.*;
import com.knotted.entity.*;
import com.knotted.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final StoreItemService storeItemService;

    // 회원 이메일 및 매장 ID로 Cart 찾는다. 있으면 해당 Cart의 매장 ID와 비교한다
    // 장바구니가 없거나 해당 매장 ID와 같은 경우 true 반환, 아니면 false 반환
    public boolean storeCheck(String memberEmail, Long storeId){
        Member member = memberRepository.findByEmail(memberEmail);

        Cart cart = cartRepository.findByMember(member);

        if(cart == null){
            // 아직 장바구니가 없는 경우 true 반환
            return true;
        }else{
            // 장바구니가 있는 경우 해당 cart의 store와 같은지 확인하고, 다르면 false를 반환한다
            if(cart.getStore().getId() == storeId){
                return true;
            }else{
                return false;
            }
        }
    }

    // 장바구니 생성 및 장바구니 상품 추가
    // 잘못되면 exception 던질 거라 void로 하겠음
    public void addToCart(String memberEmail, Long storeId, Long itemId, LocalDateTime reserveDate, Long count){
        Member member = memberRepository.findByEmail(memberEmail);

        // 매장, 상품 존재하는지 유효성 검사
        Store store = storeRepository.findById(storeId)
                .orElseThrow(EntityNotFoundException::new);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // 장바구니가 있는지 없는지부터 확인한다.
        Cart savedCart = cartRepository.findByMember(member);
        if(savedCart == null){ // 없으면
            // 장바구니를 생성하고 장바구니 상품을 추가한다

            // 장바구니 생성
            Cart cart = Cart.createCart(member, store, reserveDate);
            cartRepository.save(cart);
            // 장바구니 상품 추가
            CartItem cartItem = CartItem.createCartItem(cart, item, count);
            cartItemRepository.save(cartItem);

        }else{ // 장바구니가 있으면 (savedCart 이용)
            // 기존 장바구니의 storeId
            Long savedStoreId = savedCart.getStore().getId();

            // 해당 장바구니의 매장이 현재 추가하려는 매장과 같은지 확인한다
            if(savedStoreId == storeId){ // 장바구니의 매장이 추가하려는 것과 같으면
                // 추가하려는 장바구니 상품이 현재 장바구니에 있는지 확인한다
                CartItem savedCartItem = cartItemRepository.findByCartAndItem(savedCart, item);

                if(savedCartItem == null){ // 추가하려는 장바구니 상품이 없으면
                    // 장바구니 상품 추가
                    CartItem cartItem = CartItem.createCartItem(savedCart, item, count);
                    cartItemRepository.save(cartItem);
                    
                }else{ // 추가하려는 장바구니 상품이 이미 있으면
                    // 추가하려는 장바구니 상품의 개수를 추가한다
                    savedCartItem.addCount(count);
                }

                // 지금 주문한 예약일시로 Cart 엔티티를 업데이트한다
                savedCart.updateReserveDate(reserveDate);

            }else{ // 장바구니의 매장이 추가하려는 것과 다르면
                // 기존의 장바구니 및 장바구니 상품을 삭제한다
                cartRepository.delete(savedCart); // 이렇게만 해도 Cascade 옵션에 의해 cartItem도 삭제된다.
                
                // 장바구니를 생성하고 장바구니 아이템을 추가한다

                // 장바구니 생성
                Cart cart = Cart.createCart(member, store, reserveDate);
                cartRepository.save(cart);
                // 장바구니 상품 추가
                CartItem cartItem = CartItem.createCartItem(cart, item, count);
                cartItemRepository.save(cartItem);
            }
        }
    }

    // 해당 회원의 장바구니 리스트를 조회하여 반환
    public List<CartItemDTO> getCartItems(String memberEmail){
        Member member = memberRepository.findByEmail(memberEmail);
        Cart cart = cartRepository.findByMember(member);

        List<CartItemDTO> cartItemDTOList = new ArrayList<>();

        List<CartItem> cartItemList = cartItemRepository.findAllByCart(cart);

        for(CartItem cartItem : cartItemList){
            // 하나하나 DTO로 처리하면서 가져와야 됨. (특히 상품 사진 및 정보 필요)
            // CartItemDTO 안의 cartDTO는 필요없을 듯

            // 사진도 같이 가져오려면 아래와 같이 만들어두었던 메소드 이용하자
            ItemDTO itemDTO = itemService.convertToItemDTO(cartItem.getItem());

            CartItemDTO cartItemDTO = new CartItemDTO();
            cartItemDTO.setId(cartItem.getId());
            cartItemDTO.setItemDTO(itemDTO);
            cartItemDTO.setCount(cartItem.getCount());

            // 해당 매장 상품의 재고량을 확인하여 cartItemDTO에 넣을 것이다.
            StoreItemDTO storeItemDTO = storeItemService.getStoreItemByStoreIdAndItemId(cart.getStore().getId(), itemDTO.getId());
            cartItemDTO.setStock(storeItemDTO.getStock());

            // 만약 현재 담겨있는 값이 매장 상품에 있는 재고량보다 많을 경우, changeCount로 최대 재고량까지만 담을 수 있게 업데이트한다
            // 만약 해당 매장 상품 재고량이 0이거나 없는 경우, 장바구니에서 삭제한다
            if(storeItemDTO == null || storeItemDTO.getStock() == 0){
                cartItemRepository.delete(cartItem);
            }if(cartItem.getCount() > storeItemDTO.getStock()){
                cartItem.updateCount(storeItemDTO.getStock());
                cartItemDTO.setCount(cartItem.getCount());
            }

            cartItemDTOList.add(cartItemDTO);
        }

        return cartItemDTOList;
    }

    // 장바구니 정보에 매장 정보 얹어서 반환
    // 리팩토링 - CartItemDTO 리스트까지 넣는다
    public CartDTO getCart(String memberEmail){
        Member member = memberRepository.findByEmail(memberEmail);
        Cart cart = cartRepository.findByMember(member);

        CartDTO cartDTO = new CartDTO();
        if(cart != null){
            cartDTO = CartDTO.of(cart);
            cartDTO.setStoreDTO(StoreDTO.of(cart.getStore())); // 매장 정보 담음

            List<CartItemDTO> cartItemList = this.getCartItems(memberEmail);

            // 총합도 계산해서 같이 넘기기
            Long totalPrice = 0L;
            for(CartItemDTO cartItemDTO : cartItemList){
                totalPrice += cartItemDTO.getItemDTO().getPrice() * cartItemDTO.getCount();
            }

            cartDTO.setCartItemDTOList(cartItemList); // 매장 상품 정보 담음
            cartDTO.setTotalPrice(totalPrice); // 전체 금액 담음
        }

        return cartDTO;
    }

    // 해당 회원의 장바구니와 장바구니 상품을 전부 삭제함
    public void deleteCart(String memberEmail){
        Member member = memberRepository.findByEmail(memberEmail);
        Cart cart = cartRepository.findByMember(member);

        cartRepository.delete(cart);
    }

    // 해당 회원의 장바구니 상품을 하나 삭제함
    public void deleteCartItem(String memberEmail, Long cartItemId){
        // 해당 장바구니 상품이 요청한 회원의 것인지 확인
        // 회원의 Cart
        Member member = memberRepository.findByEmail(memberEmail);
        Cart cart = cartRepository.findByMember(member);

        // 요청한 장바구니 상품의 Cart
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Cart savedCart = cartItem.getCart();

        // 두 객체를 비교하여 다르면 IllegalStateException을 던진다
        if(!cart.equals(savedCart)){
            throw new IllegalStateException("다른 회원의 장바구니를 수정할 수 없습니다");
        }

        // 정상적이라면 장바구니 상품을 삭제한다
        cartItemRepository.delete(cartItem);

        // 삭제 후 장바구니 상품이 하나도 없을 경우 해당 회원의 장바구니도 삭제한다
        // 여기까지 왔다는 건 위의 Cart가 회원의 것이란 것이므로
        List<CartItem> cartItemList = cartItemRepository.findAllByCart(cart);

        // 만약 해당 회원의 장바구니 상품이 하나도 없으면 해당 회원의 장바구니도 삭제한다
        if(cartItemList.size() == 0){
            cartRepository.delete(cart);
        }
    }

    // 해당 회원의 장바구니 상품 개수 변경
    public void changeCount(String memberEmail, Long cartItemId, Long count){
        // 해당 장바구니 상품이 요청한 회원의 것인지 확인
        // 회원의 Cart
        Member member = memberRepository.findByEmail(memberEmail);
        Cart cart = cartRepository.findByMember(member);

        // 요청한 장바구니 상품의 Cart
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);
        Cart savedCart = cartItem.getCart();

        // 두 객체를 비교하여 다르면 IllegalStateException을 던진다
        if(!cart.equals(savedCart)){
            throw new IllegalStateException("다른 회원의 장바구니를 수정할 수 없습니다");
        }

        // 정상적이라면 업데이트한다
        cartItem.updateCount(count);

    }

}



