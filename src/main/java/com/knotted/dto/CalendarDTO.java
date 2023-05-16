package com.knotted.dto;

import lombok.Data;

import java.util.List;

// 예약 - 일자선택에서 달력에 뿌릴 정보를 가진 DTO

@Data
public class CalendarDTO {

    // 년 정보
    private int year;

    // 월 정보
    private int month;

    // 일 정보
    private List<DayInfoDTO> days;

}
