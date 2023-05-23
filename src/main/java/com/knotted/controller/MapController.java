package com.knotted.controller;

import com.knotted.dto.StoreDTO;
import com.knotted.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/map")
@Controller
@RequiredArgsConstructor
public class MapController {

    private final StoreService storeService;
    
    // MAP 메인 페이지. 매장 리스트도 읽어옴
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        // 매장 리스트를 여기서 읽지 말고, JSON 형태로 받아야 하기에 REST로 따로 받아서 처리한다.

        return "/map/index";
    }
}
