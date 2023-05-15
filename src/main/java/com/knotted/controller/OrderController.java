package com.knotted.controller;

import com.knotted.entity.Store;
import com.knotted.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@RequestMapping("/order")
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final StoreRepository storeRepository;

    // 주문(예약) 메인으로 이동
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        return "/order/index";
    }

    // 예약 - 매장선택 페이지로 이동
    @GetMapping(value = "/store-pick")
    public String storePick(Model model){
        String mode = "store";

        model.addAttribute("mode", mode);
        return "/order/orderSelect";
    }
    
    // 예약 - 일자선택 페이지로 이동
    @GetMapping(value = "/date-pick")
    public String datePick(@RequestParam("storeId") Long storeId, Model model){
        String mode = "date";
        model.addAttribute("mode", mode);

        // storeId를 받아서 해당 storeId에 해당하는 매장이 있는지 확인한다.
        if(storeId == null){
            model.addAttribute("errorMessage", "잘못된 접근입니다");
            return "/order/orderSelect";
        }

        Optional<Store> store = storeRepository.findById(storeId);
        if(store.isEmpty()){
            model.addAttribute("errorMessage", "선택하신 매장은 없는 매장입니다");
            return "/order/orderSelect";
        }

        // 다른 값은 필요 없고 넘길 때 매장명만 넘기면 될 듯
        // 옵셔널 객체는 값이 있을 수도 있고 없을 수도 있기 때문에 .get()으로 먼저 접근해줘야 한다
        model.addAttribute("storeName", store.get().getName());

        // 여기서 달력 날짜도 계산해서 같이 넘겨주어야 한다.


        return "/order/orderSelect";
    }

    // 예약 - 메뉴선택 페이지로 이동
    @GetMapping(value = "/menu-pick")
    public String menuPick(Model model){
        String mode = "menu";
        model.addAttribute("mode", mode);

        

        return "/order/orderSelect";
    }

}
