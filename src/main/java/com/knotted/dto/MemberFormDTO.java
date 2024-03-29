package com.knotted.dto;

import com.knotted.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;

@Data
public class MemberFormDTO {

    // 비밀번호
    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    @Size(min = 8, max = 16, message = "비밀번호는 8자 이상, 16자 이하로 입력해주세요")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).+", message = "비밀번호는 숫자, 영문 대소문자, 특수문자 @#$%^&+=! 중 하나를 포함하여 입력해주세요")
    private String password;

    // 이메일 (아이디처럼 사용)
    @NotBlank(message = "이메일은 필수 입력값입니다")
    @Email(message = "이메일 형식으로 입력해주세요")
    private String email;

    // 이름
    @NotBlank(message = "이름은 필수 입력값입니다")
    @Size(max = 6, message = "이름은 6자 이내로 입력해주세요")
    @Pattern(regexp = "^[a-zA-Zㄱ-ㅎ가-힣]*$", message = "이름은 한글, 영문 대소문자만 가능합니다")
    private String name;

    // 우편번호
    @NotBlank(message = "우편번호는 필수 입력값입니다")
    @Size(max = 6, message = "우편번호는 최대 6자리입니다")
    @Pattern(regexp = "^[0-9]*$", message = "우편번호는 숫자만 입력해주세요")
    private String postcode;

    // 주소
    @NotBlank(message = "주소는 필수 입력값입니다")
    @Size(max = 100, message = "주소는 100자 이내로 입력해주세요")
    private String address;

    // 주소 좌표
    private Point coordinate;

    // 위도 데이터 전달용
    private Double latitude;
    // 경도 데이터 전달용
    private Double longitude;

    // 현재 비밀번호 (회원정보 수정용)
    private String nowPassword;

    // 엔티티 <-> DTO간 변환에 사용할 ModelMapper 객체
    private static ModelMapper modelMapper = new ModelMapper();

    // 엔티티를 DTO로 변환하여 리턴
    public static MemberFormDTO of(Member member){
        return modelMapper.map(member, MemberFormDTO.class);
    }
}
