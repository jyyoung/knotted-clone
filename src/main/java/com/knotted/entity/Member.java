package com.knotted.entity;

import com.knotted.constant.MemberRole;
import com.knotted.dto.MemberFormDTO;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    @Column(name = "member_email", unique = true) // 유일값. 로그인 시 아이디처럼 사용할 것임
    private String email;

    // 이름
    @Column(name = "member_name")
    private String name;

    // 주소
    @Column(name = "member_address")
    private String address;

    // 회원 등급
    // 기본값으로 USER를 줄 거고 ADMIN은 별도로 가입하게 할 예정
    @Column(name = "member_role")
    @Enumerated(EnumType.STRING) // enum 사용 시 문제 예방하기 위해 String으로 바꿈
    private MemberRole memberRole;

    // 생성자 대신 사용할 메서드. 이렇게 구성하면 다양한 데이터를 받아 Member 객체를 생성할 수 있다. 객체지향적인 설계 방법 중 하나이다
    public static Member createMember(MemberFormDTO memberFormDTO, PasswordEncoder passwordEncoder){
        Member member = new Member();

        String password = passwordEncoder.encode(memberFormDTO.getPassword());
        member.setPassword(password);
        member.setEmail(memberFormDTO.getEmail());
        member.setName(memberFormDTO.getName());
        member.setAddress(memberFormDTO.getAddress());
        member.setMemberRole(MemberRole.USER);

        return member;
    }
}
