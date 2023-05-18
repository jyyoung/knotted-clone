package com.knotted.controller;

import com.knotted.dto.*;
import com.knotted.entity.Member;
import com.knotted.entity.Store;
import com.knotted.repository.MemberRepository;
import com.knotted.repository.StoreItemRepository;
import com.knotted.repository.StoreRepository;
import com.knotted.service.CartService;
import com.knotted.service.ItemService;
import com.knotted.service.OrderService;
import com.knotted.service.StoreItemService;
import com.knotted.util.TimeUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RequestMapping("/order")
@Controller
@RequiredArgsConstructor
public class OrderController {

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final ItemService itemService;
    private final StoreItemService storeItemService;
    private final OrderService orderService;
    private final CartService cartService;
    private final StoreItemRepository storeItemRepository;

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

    // 결제(주문) 페이지로 이동
    // 주문이긴 하지만 Cart의 서비스 등을 끌어쓸 일이 많기에 Cart 쪽에 작성하였다.
    @GetMapping(value = "/pay")
    public String orderForm(Model model, Principal principal){
        // 장바구니에 상품이 없으면 /order/store-pick으로 보내기
        String memberEmail = principal.getName();

        Member member = memberRepository.findByEmail(memberEmail);
        MemberDTO memberDTO = MemberDTO.of(member);
        CartDTO cartDTO = cartService.getCart(memberEmail);

        // 장바구니 상품이 하나도 없을 때
        if(cartDTO.getCartItemDTOList() == null){
            return "redirect:/order/store-pick";
        }

        String reserveDate = TimeUtils.localDateTimeToString(cartDTO.getReserveDate());

        model.addAttribute("member", memberDTO);
        model.addAttribute("cartDTO", cartDTO);
        model.addAttribute("reserveDate", reserveDate);

        return "/order/orderForm";
    }

    // 주문 결제 처리
    @PostMapping(value = "/new")
    @ResponseBody
    public ResponseEntity<OrderResponseDTO> orderSubmit(@RequestParam("paperbag") boolean paperbag, @RequestParam("useReward") Long useReward, Principal principal){

        // 또, 매장 상품 재고가 어떤 주문상품이 초과했는지 알려주기 위해 OrderResponseDTO를 넘겨줄 것이다.
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();

        try {
            // 회원 정보만 알면 해당 회원의 장바구니 및 장바구니 상품 정보 알 수 있다.
            // 넘어올 정보는 종이쇼핑백 사용 여부와 적립금 사용액이다.

            // 회원의 구매 금액(결제 금액으로)을 증가시키고, 적립금은 결제 금액의 5%를 주고
            // 해당 매장 상품의 재고를 감소시키고
            // 해당 상품의 판매량을 증가시키고
            // 해당 회원의 장바구니 및 장바구니 상품을 전부 삭제하고
            // 주문 및 주문 상품을 생성한다

            // 유효성 검사는, 만약 결제할 장바구니 상품이 없을 때
            // 구매할 장바구니 상품 중 하나라도 매장 상품 재고보다 모자랄 때

            String memberEmail = principal.getName();

            List<Long> errorCartItemList = orderService.createOrder(memberEmail, paperbag, useReward);

            // 만약 재고보다 많이 주문한 상품이 있으면 에러로 처리한다
            if(errorCartItemList.size() > 0){
                orderResponseDTO.setSuccess(false);
                orderResponseDTO.setErrorCartItemList(errorCartItemList);
                orderResponseDTO.setErrorMessage("재고를 초과하여 주문한 상품이 있습니다. 다시 한 번 확인해주세요");

                return new ResponseEntity<>(orderResponseDTO, HttpStatus.OK);
            }

            // 여기까지 정상적으로 온 경우 정상 처리로 반환한다
            orderResponseDTO.setSuccess(true);
            return new ResponseEntity<>(orderResponseDTO, HttpStatus.OK);

        } catch (IllegalStateException e) {
            orderResponseDTO.setSuccess(false);
            orderResponseDTO.setErrorMessage("결제할 상품이 없습니다");

            // OrderResponseDTO에 에러 메시지를 담기 위해 HttpStatus.OK로 반환함
            return new ResponseEntity<>(orderResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            orderResponseDTO.setSuccess(false);
            orderResponseDTO.setErrorMessage("결제 처리 중 에러가 발생했습니다");

            return new ResponseEntity<>(orderResponseDTO, HttpStatus.OK);
        }
    }

    // 주문 완료 안내 페이지로 이동
    @GetMapping(value = "/complete")
    public String complete(){
        return "/order/complete";
    }

}
