package com.knotted.entity;

import com.knotted.constant.MemberRole;
import jakarta.persistence.*;
import lombok.Data;

// 회원 엔티티

@Entity
@Table(name="member")
@Data
public class Member extends BaseEntity{

    // 회원 ID
    @Id
    @Column(name = "member_id")
    private Long id; // 기본키 PK의 변수명은 그냥 id로 하는 것이 관례 (다른 엔티티도 마찬가지)

    // 비밀번호
    @Column(name = "member_password")
    private String password;

    // 이메일 (중복 불가)
    @Column(name = "member_email", unique = true) // 유일값
    private String email;

    // 이름
    @Column(name = "member_name")
    private String name;

    // 주소
    @Column(name = "member_address")
    private String address;

    // 회원 등급
    @Column(name = "member_role")
    @Enumerated(EnumType.STRING) // enum 사용 시 문제 예방하기 위해 String으로 바꿈
    private MemberRole role;
}
