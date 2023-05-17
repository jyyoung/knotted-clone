package com.knotted.service;

import com.knotted.dto.CartDTO;
import com.knotted.dto.CartItemDTO;
import com.knotted.dto.ItemDTO;
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
            cartItemDTO.setItemDTO(itemDTO);
            cartItemDTO.setCount(cartItem.getCount());

            cartItemDTOList.add(cartItemDTO);
        }

        return cartItemDTOList;
    }

    public CartDTO getCart(String memberEmail){
        Member member = memberRepository.findByEmail(memberEmail);
        Cart cart = cartRepository.findByMember(member);
        CartDTO cartDTO = CartDTO.of(cart);

        return cartDTO;
    }

}



