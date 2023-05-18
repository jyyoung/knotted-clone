package com.knotted.service;

import com.knotted.constant.OrderStatus;
import com.knotted.dto.CartDTO;
import com.knotted.dto.CartItemDTO;
import com.knotted.dto.OrderResponseDTO;
import com.knotted.entity.*;
import com.knotted.repository.*;
import jakarta.persistence.EntityNotFoundException;
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
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final StoreItemRepository storeItemRepository;
    private final ItemRepository itemRepository;
    private final StoreRepository storeRepository;
    private final OrderItemRepository orderItemRepository;

    // 주문 생성 (각종 유효성 검사도 함)
    public OrderResponseDTO createOrder(String memberEmail, boolean paperbag, Long useReward){

        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();

        // 일단 회원 정보로 장바구니 및 장바구니 상품 정보 가져오기
        Member member = memberRepository.findByEmail(memberEmail);
        CartDTO cartDTO = cartService.getCart(memberEmail);

        // 결제할 장바구니 상품이 없을 때
        if(cartDTO.getCartItemDTOList() == null){
            throw new IllegalStateException("결제할 상품이 없습니다");
        }

        Long storeId = cartDTO.getStoreDTO().getId();
        Store store = storeRepository.findById(storeId)
                .orElseThrow(EntityNotFoundException::new);

        // 매장 상품 재고보다 많이 주문한 상품들을 저장하기 위한 리스트
        List<Long> errorCartItemList = new ArrayList<>();
        orderResponseDTO.setErrorCartItemList(errorCartItemList); // 일단 빈 거 넣음
        
        Long totalPrice = 0L; // 전체 결제 금액 초기화

        // 장바구니 상품들을 순회하면서 매장 상품 재고보다 많이 주문한 게 있는지 확인 및 재고 감소 처리
        for(CartItemDTO cartItemDTO : cartDTO.getCartItemDTOList()){
            Long orderCount = cartItemDTO.getCount();
            Long itemId = cartItemDTO.getItemDTO().getId();

            // storeId와 itemId로 실제 매장의 재고 확인
            StoreItem storeItem = storeItemRepository.findByStoreIdAndItemId(storeId, itemId);
            Long stock = storeItem.getStock();

            // 해당 상품 엔티티 조회
            Item item = itemRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);
            
            // 해당 상품 판매량을 증가시킨다
            item.addSaleCount(orderCount);

            if(orderCount > stock){
                // 재고를 초과한 장바구니 상품 ID를 저장함
                errorCartItemList.add(cartItemDTO.getId());
            }else{
                // 해당 매장 상품의 재고를 감소시킨다
                storeItem.subtractStock(orderCount);
            }

            totalPrice += cartItemDTO.getItemDTO().getPrice() * cartItemDTO.getCount();
        }
        
        // 만약 재고보다 많이 주문한 상품이 있으면 주문 처리하지 않고 리스트를 리턴함
        if(errorCartItemList.size() > 0){
            orderResponseDTO.setSuccess(false);
            orderResponseDTO.setErrorCartItemList(errorCartItemList);
            return orderResponseDTO;
        }
        
        // 유효성 처리 끝

        // 여기까지 제대로 왔으면 주문 처리를 함

        // 회원이 적립금을 사용했으면 회원의 적립금을 감소시켜야 함 - OK
        // 회원의 구매 금액(결제 금액으로)을 증가시킨다 - OK
        // 회원 적립금은 지금 결제한 금액의 5%를 준다 - OK
        // 해당 매장 상품의 재고를 감소시킨다 - OK
        // 해당 상품의 판매량을 증가시킨다 - OK
        // 주문 및 주문 상품을 생성한다
        // 마지막으로 해당 회원의 장바구니 및 장바구니 상품을 전부 삭제한다 (장바구니 삭제만 하면 상품은 같이 지워짐)
        
        // 일단 paperbag, useReward를 이용해서 결제 금액을 계산한다
        if(paperbag){
            // 종이 쇼핑백 300원으로 설정함
            totalPrice += 300;
        }
        if(useReward > 0){
            totalPrice -= useReward;
            
            // 회원 적립금 감소시킨다
            member.subtractReward(useReward);
        }
        
        // 회원의 구매금액을 최종 결제금액으로 증가시킨다
        member.addPurchase(totalPrice);

        // 최종 결제금액의 5% 계산한다
        Long reward = Math.round(totalPrice * 0.05);

        // 회원 적립금 증가시킨다
        member.addReward(reward);

        // 주문을 생성한다
        Order order = new Order();
        order.setMember(member);
        order.setStore(store);
        order.setPaperBagUsed(paperbag);
        order.setOrderPrice(totalPrice);
        order.setOrderStatus(OrderStatus.ORDER);
        order.setReserveDate(cartDTO.getReserveDate());

        orderRepository.save(order); // 주문 생성

        // 이 생성된 주문 번호로 주문 상품들 생성할 것
        Long orderId = order.getId();

        // 주문 상품을 생성한다
        for(CartItemDTO cartItemDTO : cartDTO.getCartItemDTOList()){

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);

            Long itemId = cartItemDTO.getItemDTO().getId();

            Item item = itemRepository.findById(itemId)
                    .orElseThrow(EntityNotFoundException::new);

            orderItem.setItem(item);
            orderItem.setName(item.getName());
            orderItem.setPrice(item.getPrice());
            orderItem.setCount(cartItemDTO.getCount());

            orderItemRepository.save(orderItem);
        }
        
        // 장바구니 및 장바구니 상품을 삭제한다
        // 주의!!! 아래와 같이 불렀을 경우 조회는 문제 없지만 실제 데이터를 조작하는 경우,
        // 독립적인 트랜잭션 컨텍스트에서 사용 시 실패하더라도 다른 데이터가 롤백하지 않는 문제가 있다.
        // 그러므로 이건 현재 Service 내부에서 실행하도록 하겠다.
//        cartService.deleteCart(memberEmail);
        Cart cart = cartRepository.findByMember(member);
        cartRepository.delete(cart);

        // 여기까지 정상적으로 왔으면 빈 에러 카트 리스트를 반환한다
        orderResponseDTO.setSuccess(true);
        orderResponseDTO.setOrderId(orderId);
        return orderResponseDTO;
    }

}
