package com.knotted.dto;

import lombok.Data;

// 예약 - 일자선택에서 달력에 뿌릴 정보 중 날짜 정보를 갖고 있는 DTO
@Data
public class DayInfoDTO {
    
    // 날짜 (일)
    private int date;
    
    // 해당 날짜가 선택 가능한지 (현재 날짜로부터 +2일부터 +7일까지 가능하게 할 것임)
    private boolean active;

    // 요일 정보는 굳이 필요없을 듯 (어차피 1일의 앞부분 채울 거라서)
}
