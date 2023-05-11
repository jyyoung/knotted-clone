package com.knotted.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalTime;

@Data
public class StoreFormDTO {

    // 매장명
    @NotBlank(message = "매장명은 필수 입력값입니다")
    private String name;

    // 전화번호
    @NotBlank(message = "전화번호는 필수 입력값입니다")
    private String phoneNumber;

    // 우편번호
    @NotBlank(message = "우편번호는 필수 입력값입니다")
    @Size(max = 6, message = "우편번호는 최대 6자리입니다")
    @Pattern(regexp = "^[0-9]*$")
    private String postcode;

    // 주소
    @NotBlank(message = "주소는 필수 입력값입니다")
    private String address;

    // 영업시작시간
    @NotBlank(message = "영업시작시간은 필수 입력값입니다")
    private LocalTime openTime;

    // 영업종료시간
    @NotBlank(message = "영업종료시간은 필수 입력값입니다")
    private LocalTime closeTime;

    // 매장 설명
    private String description;

}
