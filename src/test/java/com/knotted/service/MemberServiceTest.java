package com.knotted.service;

import com.knotted.dto.MemberFormDTO;
import com.knotted.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // MemberFormDTO로 Member 엔티티 만드는 메소드
    private Member createMember(){
        MemberFormDTO memberFormDTO = new MemberFormDTO();
        memberFormDTO.setPassword("1234");
        memberFormDTO.setEmail("test@test.com");
        memberFormDTO.setName("정민");
        memberFormDTO.setAddress("서울");

        return Member.createMember(memberFormDTO, passwordEncoder);
    }
    
    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest(){
        Member member = this.createMember();
        Member savedMember = memberService.saveMember(member);

        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getPostcode(), savedMember.getPostcode());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getMemberRole(), savedMember.getMemberRole());

    }
}
