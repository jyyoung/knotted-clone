package com.knotted.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MemberFormDTO {

    // 비밀번호
    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    @Size(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).+")
    private String password;

    // 이메일 (아이디처럼 사용)
    @NotBlank(message = "이메일은 필수 입력값입니다")
    @Email(message = "이메일 형식으로 입력해주세요")
    private String email;

    // 이름
    @NotBlank(message = "이름은 필수 입력값입니다")
    @Size(max = 6, message = "이름은 6자 이내로 입력해주세요")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣]*$")
    private String name;

    // 우편번호
    @NotBlank(message = "우편번호는 필수 입력값입니다")
    @Size(max = 6, message = "우편번호는 최대 6자리입니다")
    @Pattern(regexp = "^[0-9]*$")
    private String postcode;

    // 주소
    @NotBlank(message = "주소는 필수 입력값입니다")
    @Size(max = 100, message = "주소는 100자 이내로 입력해주세요")
    private String address;
}
