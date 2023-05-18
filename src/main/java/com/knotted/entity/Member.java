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
    @GeneratedValue // 주의!! strategy 속성은 생략해도 @GeneratedValue 자체는 생략하면 안 됨!!
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

    // 우편번호
    @Column(name = "member_postcode")
    private String postcode;

    // 주소
    @Column(name = "member_address")
    private String address;

    // 총 구매금액
    @Column(name = "member_purchase")
    private Long purchase;

    // 적립금
    @Column(name = "member_reward")
    private Long reward;

    // 사용한 적립금
    @Column(name = "member_reward_use")
    private Long rewardUse;

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
        member.setPostcode(memberFormDTO.getPostcode());
        member.setAddress(memberFormDTO.getAddress());
        member.setPurchase(0L);
        member.setReward(0L);
        member.setRewardUse(0L);
        member.setMemberRole(MemberRole.USER);
//        member.setMemberRole(MemberRole.ADMIN); // 일단 테스트를 위해 ADMIN 롤로 가입

        return member;
    }

    // 회원의 구매금액을 증가시키는 메소드
    public void addPurchase(Long orderPrice){
        this.purchase += orderPrice;
    }

    // 회원의 적립금을 증가시키는 메소드
    public void addReward(Long reward){
        this.reward += reward;
    }
    
    // 회원의 적립금을 감소시키는 메소드
    public void subtractReward(Long reward){
        this.reward -= reward;
    }
    
    // 회원의 사용 적립금을 증가시키는 메소드
    public void addRewardUse(Long rewardUse){
        this.rewardUse -= rewardUse;
    }
}
