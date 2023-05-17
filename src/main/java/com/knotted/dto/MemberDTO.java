package com.knotted.dto;

import com.knotted.constant.MemberRole;
import lombok.Data;

@Data
public class MemberDTO {

    // 회원 ID
    private Long id; // 기본키 PK의 변수명은 그냥 id로 하는 것이 관례 (다른 엔티티도 마찬가지)

    // 비밀번호
    private String password;

    // 이메일 (중복 불가)
    private String email;

    // 이름
    private String name;

    // 우편번호
    private String postcode;

    // 주소
    private String address;

    // 회원 등급
    private MemberRole memberRole;
}
