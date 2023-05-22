package com.knotted.dto;

import com.knotted.constant.MemberRole;
import com.knotted.entity.Member;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;

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

    // 주소 좌표
    private Point coordinate;

    // 총 구매금액
    private Long purchase;

    // 적립금
    private Long reward;

    // 사용한 적립금
    private Long rewardUse;

    // 회원 등급
    private MemberRole memberRole;

    // 탈퇴 여부
    private boolean withdraw;

    // 탈퇴 사유
    private String withdrawReason;

    // 비밀번호 변경 토큰
    private String passwordToken;

    // 엔티티 <-> DTO간 변환에 사용할 ModelMapper 객체
    private static ModelMapper modelMapper = new ModelMapper();

    // 엔티티 -> DTO 변환
    public static MemberDTO of(Member member){
        return modelMapper.map(member, MemberDTO.class);
    }
}
