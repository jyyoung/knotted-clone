package com.knotted.service;

import com.knotted.constant.OrderCancelType;
import com.knotted.constant.OrderStatus;
import com.knotted.dto.*;
import com.knotted.entity.*;
import com.knotted.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemService orderItemService;
    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final StoreRepository storeRepository;
    private final StoreItemRepository storeItemRepository;
    private final ItemRepository itemRepository;
    private final RewardHistoryRepository rewardHistoryRepository;

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
        // 주문 및 주문 상품을 생성한다 - OK
        // 마지막으로 해당 회원의 장바구니 및 장바구니 상품을 전부 삭제한다 - OK

        // 일단 paperbag, useReward를 이용해서 결제 금액을 계산한다

        if(paperbag){
            // 종이 쇼핑백 300원으로 설정함
            totalPrice += 300;
        }

        Long orderPrice = totalPrice; // 결제 금액 (적립금 사용 후)

        // 적립금 사용하였으면
        if(useReward > 0){
            orderPrice -= useReward;
            
            // 회원 적립금 감소시킨다
            member.subtractReward(useReward);

            // 회원 사용 적립금 증가시킨다
            member.addRewardUse(useReward);
        }
        
        // 회원의 구매금액을 최종 결제금액으로 증가시킨다
        member.addPurchase(orderPrice);

        // 최종 결제금액의 5% 계산한다
        Long reward = Math.round(orderPrice * 0.05);

        // 회원 적립금 증가시킨다
        member.addReward(reward);

        // 주문을 생성한다
        Order order = new Order();
        order.setMember(member);
        order.setStore(store);
        order.setPaperBagUsed(paperbag);
        order.setTotalPrice(totalPrice);
        order.setOrderPrice(orderPrice);
        order.setStatus(OrderStatus.ORDER);
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

        // 적립 내역 생성

        // 적립금 사용하였으면
        if(useReward > 0){
            // 적립금 사용 내역을 생성한다
            RewardHistory rewardHistory = new RewardHistory();
            rewardHistory.setMember(member);
            rewardHistory.setOrder(order);
            rewardHistory.setStore(store);
            rewardHistory.setType(true); // 사용은 1(true)
            rewardHistory.setPoint(useReward);

            rewardHistoryRepository.save(rewardHistory); // 적립금 사용 내역 생성
        }

        // 적립금 획득 내역을 생성한다
        RewardHistory acquireRewardHistory = new RewardHistory();
        acquireRewardHistory.setMember(member);
        acquireRewardHistory.setOrder(order);
        acquireRewardHistory.setStore(store);
        acquireRewardHistory.setType(false); // 획득은 0(false)
        acquireRewardHistory.setPoint(reward);

        rewardHistoryRepository.save(acquireRewardHistory); // 적립금 획득 내역 생성
        
        // 장바구니 및 장바구니 상품을 삭제한다
        // 주의!!! 아래와 같이 불렀을 경우 조회는 문제 없지만 실제 데이터를 조작하는 경우,
        // 독립적인 트랜잭션 컨텍스트에서 사용 시 실패하더라도 다른 데이터가 롤백하지 않는 문제가 있다.
        // 그러므로 이건 현재 Service 내부에서 실행하도록 하겠다.
//        cartService.deleteCart(memberEmail);
        Cart cart = cartRepository.findByMember(member);
        cartRepository.delete(cart);

        // 여기까지 정상적으로 왔으면 빈 에러 카트 리스트를 반환한다
        orderResponseDTO.setOrderId(orderId);
        return orderResponseDTO;
    }

    // 주문 취소(삭제) 처리
    public void cancelOrder(String memberEmail, Long orderId, OrderCancelType cancelType, String cancelDescription){

        // 해당 주문으로 인해 받은 적립금이 있는지 확인 후,
        // 해당 적립금 획득 내역을 삭제한다
        // 해당 회원의 적립금을 감소시킨다.
        // 또한 해당 회원의 구매금액을 결제금액만큼 감소시킨다.
        
        // 해당 주문에 사용된 적립금이 있는지 확인 후,
        // 해당 적립금 사용 내역을 삭제한다.
        // 해당 회원의 적립금을 증가시킨다.
        // 해당 회원의 사용 적립금을 감소시킨다.
        
        // 해당 주문의 주문 상태를 CANCEL로 바꾸고, 예약 취소 사유 및 예약 취소 상세사유를 설정한다.

        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        Store store = order.getStore();
        if(store == null){
            throw new EntityNotFoundException();
        }

        // 취소 요청된 주문이 현재 사용자의 주문인지 확인
        Member member = memberRepository.findByEmail(memberEmail);
        Member savedMember = order.getMember();
        if(!member.equals(savedMember)){
            throw new IllegalStateException();
        }

        // 이미 취소된 요청인지 확인
        if(order.getStatus().equals("CANCEL")){
            throw new IllegalStateException();
        }

        // 해당 주문의 적립금 획득 내역 조회
        RewardHistory acquireRewardHistory = rewardHistoryRepository.findByOrderAndType(order, false);
        if(acquireRewardHistory != null){
            Long acquireReward = acquireRewardHistory.getPoint();
            Long orderPrice = order.getOrderPrice();

            // 회원의 적립금 감소
            member.subtractReward(acquireReward);

            // 회원의 구매금액을 결제금액만큼 감소
            member.subtractPurchase(orderPrice);

            // 적립금 획득 내역 삭제
            rewardHistoryRepository.delete(acquireRewardHistory);
        }

        // 해당 주문의 적립금 사용 내역 조회
        RewardHistory useRewardHistory = rewardHistoryRepository.findByOrderAndType(order, true);
        if(useRewardHistory != null){
            Long useReward = useRewardHistory.getPoint();

            // 회원의 적립금 증가
            member.addReward(useReward);

            // 회원의 사용 적립금 감소
            member.subtractRewardUse(useReward);

            // 적립금 사용 내역 삭제
            rewardHistoryRepository.delete(useRewardHistory);
        }

        // 해당 주문의 주문 상품을 순회하면서 해당 매장의 재고를 다시 증가시켜야 한다!
        List<OrderItemDTO> orderItemDTOList = orderItemService.getOrderItems(orderId);
        for(OrderItemDTO orderItemDTO : orderItemDTOList){
            Long itemId = orderItemDTO.getItemDTO().getId();
            Long count = orderItemDTO.getCount();
            StoreItem storeItem = storeItemRepository.findByStoreIdAndItemId(store.getId(), itemId);

            storeItem.addStock(count); // 해당 매장의 재고를 증가시킨다
        }

        // 주문 업데이트
        order.updateOrder(OrderStatus.CANCEL, cancelType, cancelDescription);

    }

    // 해당 회원의 주문 조회
    public List<OrderDTO> getOrders(String memberEmail){

        Member member = memberRepository.findByEmail(memberEmail);
        List<Order> orderList = orderRepository.findByMember(member, Sort.by(Sort.Direction.DESC, "id"));

        List<OrderDTO> orderDTOList = new ArrayList<>();

        for(Order order : orderList){
            OrderDTO orderDTO = OrderDTO.of(order);
            
            // 주문 상품 리스트 정보 넣기
            List<OrderItemDTO> orderItemDTOList = orderItemService.getOrderItems(orderDTO.getId());
            orderDTO.setOrderItemDTOList(orderItemDTOList);
            
            // 주문한 매장 정보 넣기
            Store store = storeRepository.findById(order.getStore().getId())
                    .orElseThrow(EntityNotFoundException::new);
            StoreDTO storeDTO = StoreDTO.of(store);
            orderDTO.setStoreDTO(storeDTO);

            orderDTOList.add(orderDTO);
        }

        return orderDTOList;
    }

}
