package com.knotted.controller.admin;

import com.knotted.dto.OrderDTO;
import com.knotted.service.admin.AdminOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/admin/order")
@Controller
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    // 주문 관리 메인 (목록 조회)
    @GetMapping(value = {"", "/"})
    public String main(Model model){

        List<OrderDTO> orderDTOList = adminOrderService.getOrders();

        model.addAttribute("orderDTOList", orderDTOList);

        return "/admin/order/index";
    }


}
