package com.knotted.controller;

import com.knotted.dto.CalendarDTO;
import com.knotted.dto.DayInfoDTO;
import com.knotted.dto.ItemFormDTO;
import com.knotted.dto.StoreItemDTO;
import com.knotted.entity.Store;
import com.knotted.repository.StoreRepository;
import com.knotted.service.ItemService;
import com.knotted.service.OrderService;
import com.knotted.service.StoreItemService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RequestMapping("/order")
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final StoreRepository storeRepository;
    private final ItemService itemService;
    private final StoreItemService storeItemService;
    private final OrderService orderService;

    // 주문(예약) 메인으로 이동
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        return "/order/index";
    }

    // 해당 매장 존재 여부를 확인하고 있으면 StoreName을 반환함
    public String isStoreExists(Long storeId){
        Store store = storeRepository.findById(storeId)
                .orElseThrow(EntityNotFoundException::new);

        return store.getName();
    }

    // 해당 상품 존재 여부를 확인하고 있으면 ItemFormDTO를 반환함 (이미지 정보도 있어야 함)
    public ItemFormDTO isItemExists(Long itemId){
        ItemFormDTO itemFormDTO = itemService.getItem(itemId);

        return itemFormDTO;
    }

    // 예약 - 매장선택 페이지로 이동
    @GetMapping(value = "/store-pick")
    public String storePick(Model model){
        model.addAttribute("mode", "store");
        return "/order/orderSelect";
    }
    
    // 예약 - 일자선택 페이지로 이동
    @GetMapping(value = "/date-pick")
    public String datePick(@RequestParam("storeId") Long storeId, Model model){
        model.addAttribute("mode", "date");
        model.addAttribute("storeId", storeId);

        // 해당 매장 존재 여부 확인
        try{
            String storeName = this.isStoreExists(storeId);
            model.addAttribute("storeName", storeName);
        }catch(Exception e){
            model.addAttribute("errorMessage", "존재하지 않는 매장입니다");
            return "redirect:/order/store-pick";
        }

        // 여기서 달력 날짜도 계산해서 같이 넘겨주어야 한다.
        // 일단 오늘 날짜로부터 +2일 ~ +7일 총 6일간을 선택 가능하게 해야 하고,
        // 근데 이 부분은 REST로 설정하는 게 좋아 보인다

        return "/order/orderSelect";
    }

    // 예약 - 일자선택에서 오늘 날짜를 기준으로 달력을 만들어 반환하는 REST API
    @GetMapping(value = "/date-pick/calendar")
    @ResponseBody
    public ResponseEntity<CalendarDTO> datePickCalender(@RequestParam("year") int year, @RequestParam("month") int month){ // 뭔가 받을 필요는 없음

        // 내 생각인데 CalendarDTO를 List로 만들어서, 해당 DTO 내에 요일 정보랑 이런 거 넣는 것도 괜찮을 거라고 봄

        // 간단한 로직이므로 컨트롤러에서 구현하도록 하겠다.
        // 근데 현재 달만 보여주는 게 아니고 달력을 넘기면서 보기도 한다
        // 그래서 year, month를 입력값으로 받아서 달력 정보를 만들기로 하겠다.

        Calendar nowCalendar = Calendar.getInstance();
        int nowYear = nowCalendar.get(Calendar.YEAR); // 현재 년도 구함
        int nowMonth = nowCalendar.get(Calendar.MONTH); // 현재 달 구함
        int nowDay = nowCalendar.get(Calendar.DATE); // 현재 일자 구함
        
        if(year == 0){ // 연월 정보가 없는 경우 현재 연월 기준으로 만들도록 하겠다 (month가 0인 경우는 실제로 존재하기 때문에 쓰지 않았다. 그 외의 예외 처리는 하지 않겠다)
            // 현재 날짜 구하기
            Calendar calendar = Calendar.getInstance(); // 이렇게 하면 현재 날짜로 인스턴스 생성
            year = calendar.get(Calendar.YEAR); // 현재 년도
            month = calendar.get(Calendar.MONTH); // 현재 달 (month는 0부터 시작함)
        }

        // 해당 월의 마지막 날을 구하려면 다양한 방법이 있는데, Calendar 객체를 생성 후 연도, 월 설정 후 getActualMaximum(Calendar.DAY_OF_MONTH)를 호출하는 방법이 있다.

        // 해당 월의 마지막 날 구하기
        Calendar dayOfMonth = Calendar.getInstance();
        dayOfMonth.set(year, month, 1); // 위에서 설정한 year, month로 Calendar 객체를 설정함
        int lastDayOfMonth = dayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 연월의 마지막 날

        // 해당 월의 첫째 날이 무슨 요일인지 구하기
        int firstDayOfWeek = dayOfMonth.get(Calendar.DAY_OF_WEEK); // 해당 연월의 첫째날의 요일

        CalendarDTO calendarDTO = new CalendarDTO();
        calendarDTO.setYear(year);
        calendarDTO.setMonth(month);
        calendarDTO.setActiveExists(false); // 기본값 false

        List<DayInfoDTO> days = new ArrayList<>(); // 반환할 리스트

        // 첫째 주의 공백 처리 (1이 일요일, 7이 토요일)
        for (int i = 1; i < firstDayOfWeek; i++) { // 1일의 요일 전까지
            DayInfoDTO dayInfoDTO = new DayInfoDTO();
            dayInfoDTO.setDate(0); // 공백은 0으로 설정할 것임
            dayInfoDTO.setActive(false);
            // 요일 정보는 필요 없음 (공백을 넣었기 때문에 계산할 필요가 없음)

            days.add(dayInfoDTO);
        }

        // 해당 월의 일자 정보 추가
        for (int i = 1; i <= lastDayOfMonth; i++) {
            DayInfoDTO dayInfoDTO = new DayInfoDTO();
            dayInfoDTO.setDate(i);

            // 클릭 활성화시킬 날짜 정함
            if(year == nowYear && month == nowMonth && i >= nowDay + 2 && i <= nowDay + 7){
                dayInfoDTO.setActive(true);
                calendarDTO.setActiveExists(true); // 하나라도 active가 있으면 true
            }else{
                dayInfoDTO.setActive(false);
            }

            days.add(dayInfoDTO);
        }

        calendarDTO.setDays(days);

        return new ResponseEntity<>(calendarDTO, HttpStatus.OK);
    }

    // 예약 - 메뉴선택 페이지로 이동
    @GetMapping(value = "/menu-pick")
    public String menuPick(@RequestParam("storeId") Long storeId, @RequestParam("bookDate") String bookDate, @RequestParam("bookTime") String bookTime, Model model){ // 에러 메시지 전달용으로 세션을 사용하였다
        model.addAttribute("mode", "menu");
        model.addAttribute("storeId", storeId);
        model.addAttribute("bookDate", bookDate);
        model.addAttribute("bookTime", bookTime);

        // 해당 매장 존재 여부 확인
        try{
            String storeName = this.isStoreExists(storeId);
            model.addAttribute("storeName", storeName);
        }catch(Exception e){
            model.addAttribute("errorMessage", "존재하지 않는 매장입니다");
            return "redirect:/order/store-pick";
        }

        return "/order/orderSelect";
    }

    // 예약 - 메뉴선택 메뉴 상세 페이지로 이동
    @GetMapping(value = "/menu-pick-detail")
    public String menuPickDetail(@RequestParam("storeId") Long storeId, @RequestParam("bookDate") String bookDate, @RequestParam("bookTime") String bookTime, @RequestParam("itemId") Long itemId, Model model){
        model.addAttribute("mode", "menuDetail");
        model.addAttribute("storeId", storeId);
        model.addAttribute("bookDate", bookDate);
        model.addAttribute("bookTime", bookTime);
        model.addAttribute("itemId", itemId);

        // 해당 매장 존재 여부 확인
        try{
            String storeName = this.isStoreExists(storeId);
            model.addAttribute("storeName", storeName);
        }catch(Exception e){
            model.addAttribute("errorMessage", "존재하지 않는 매장입니다");
            return "redirect:/order/store-pick";
        }

        // 해당 상품 존재 여부 확인
        try{
            ItemFormDTO itemFormDTO = this.isItemExists(itemId);
            model.addAttribute("itemFormDTO", itemFormDTO);
        }catch(Exception e){
            model.addAttribute("errorMessage", "존재하지 않는 상품입니다");
            return "redirect:/order/store-pick";
        }

        return "/order/orderSelect";
    }

    // 해당 매장의 해당 상품의 현재 재고를 확인하여 리턴함
    @GetMapping(value = "/checkStock")
    @ResponseBody
    public ResponseEntity checkStock(@RequestParam("storeId") Long storeId, @RequestParam("itemId") Long itemId){
        // 해당 매장의 재고를 확인함
        StoreItemDTO storeItemDTO = storeItemService.getStoreItemByStoreIdAndItemId(storeId, itemId);

        if(storeItemDTO.getId() == null){
            return new ResponseEntity(0, HttpStatus.OK);
        }

        Long stock = storeItemDTO.getStock();
        return new ResponseEntity(stock, HttpStatus.OK);
    }

}
