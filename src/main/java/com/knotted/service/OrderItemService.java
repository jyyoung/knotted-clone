package com.knotted.service;

import com.knotted.dto.ItemDTO;
import com.knotted.dto.OrderItemDTO;
import com.knotted.entity.Order;
import com.knotted.entity.OrderItem;
import com.knotted.repository.OrderItemRepository;
import com.knotted.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    // 해당 주문의 모든 주문 상품을 조회함
    public List<OrderItemDTO> getOrderItems(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItemList = orderItemRepository.findByOrder(order);

        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();

        for(OrderItem orderItem : orderItemList){
            OrderItemDTO orderItemDTO = OrderItemDTO.of(orderItem);

            // OrderItemDTO 안의 ItemDTO도 같이 넣어준다
            ItemDTO itemDTO = ItemDTO.of(orderItem.getItem());
            orderItemDTO.setItemDTO(itemDTO);

            orderItemDTOList.add(orderItemDTO);
        }

        return orderItemDTOList;
    }

}



