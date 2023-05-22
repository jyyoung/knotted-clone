package com.knotted.service.admin;

import com.knotted.dto.MemberDTO;
import com.knotted.dto.OrderDTO;
import com.knotted.dto.OrderItemDTO;
import com.knotted.dto.StoreDTO;
import com.knotted.entity.Order;
import com.knotted.repository.admin.AdminOrderRepository;
import com.knotted.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor

public class AdminOrderService {
    private final AdminOrderRepository adminOrderRepository;
    private final OrderItemService orderItemService;

    // 모든 주문 조회
    public List<OrderDTO> getOrders(){
        List<Order> orderList = adminOrderRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<OrderDTO> orderDTOList = new ArrayList<>();

        for(Order order : orderList){
            OrderDTO orderDTO = OrderDTO.of(order);
            StoreDTO storeDTO = StoreDTO.of(order.getStore());
            MemberDTO memberDTO = MemberDTO.of(order.getMember());
            List<OrderItemDTO> orderItemDTOList = orderItemService.getOrderItems(order.getId());

            orderDTO.setStoreDTO(storeDTO);
            orderDTO.setMemberDTO(memberDTO);
            orderDTO.setOrderItemDTOList(orderItemDTOList);

            orderDTOList.add(orderDTO);
        }

        return orderDTOList;
    }
}







