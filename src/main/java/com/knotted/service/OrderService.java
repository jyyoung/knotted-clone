package com.knotted.service;

import com.knotted.dto.CartDTO;
import com.knotted.dto.CartItemDTO;
import com.knotted.entity.Member;
import com.knotted.entity.StoreItem;
import com.knotted.repository.MemberRepository;
import com.knotted.repository.OrderRepository;
import com.knotted.repository.StoreItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final CartService cartService;
    private final StoreItemRepository storeItemRepository;


    // 주문 생성 (각종 유효성 검사도 함)
    public List<Long> createOrder(String memberEmail){

        // 일단 회원 정보로 장바구니 및 장바구니 상품 정보 가져오기
        Member member = memberRepository.findByEmail(memberEmail);
        CartDTO cartDTO = cartService.getCart(memberEmail);

        // 결제할 장바구니 상품이 없을 때
        if(cartDTO.getCartItemDTOList() == null){
            throw new IllegalStateException("결제할 상품이 없습니다");
        }

        Long storeId = cartDTO.getStoreDTO().getId();

        // 매장 상품 재고보다 많이 주문한 상품들을 저장하기 위한 리스트
        List<Long> errorCartItemList = new ArrayList<>();

        // 장바구니 상품들을 순회하면서 매장 상품 재고보다 많이 주문한 게 있는지 확인
        for(CartItemDTO cartItemDTO : cartDTO.getCartItemDTOList()){
            Long orderCount = cartItemDTO.getCount();
            Long itemId = cartItemDTO.getItemDTO().getId();

            // storeId와 itemId로 실제 매장의 재고 확인
            StoreItem storeItem = storeItemRepository.findByStoreIdAndItemId(storeId, itemId);
            Long stock = storeItem.getStock();

            if(orderCount > stock){
                // 재고를 초과한 장바구니 상품 ID를 저장함
                errorCartItemList.add(cartItemDTO.getId());
            }
        }
        
        // 만약 재고보다 많이 주문한 상품이 있으면 주문 처리하지 않고 리스트를 리턴함
        if(errorCartItemList.size() > 0){
            return errorCartItemList;
        }

        // 그렇지 않으면 주문 처리를 함

        // 회원의 구매 금액(결제 금액으로)을 증가시키고, 적립금은 결제 금액의 5%를 주고
        // 해당 매장 상품의 재고를 감소시키고
        // 해당 상품의 판매량을 증가시키고
        // 해당 회원의 장바구니 및 장바구니 상품을 전부 삭제하고
        // 주문 및 주문 상품을 생성한다
    }

}
