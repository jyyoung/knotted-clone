package com.knotted.service;

import com.knotted.entity.Member;
import com.knotted.exception.DuplicateMemberException;
import com.knotted.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // Member 엔티티 전달받아 유효성 검사 후 DB에 반영
    public Member saveMember(Member member){
        this.validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    // Member 엔티티 전달받아 이메일 중복 검사
    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if(findMember != null){
            throw new DuplicateMemberException("이미 가입한 이메일입니다");
        }
    }

    // 이메일을 매개변수로 받아 회원 중복 검사
    public boolean checkDuplicateEmail(String email){
        Member findMember = memberRepository.findByEmail(email);

        if(findMember != null){
            return false;
        }else{
            return true;
        }
    }

    // UserDetails를 구현하는 User 객체를 반환함. 이 메소드를 통해 사용자 정보와 권한 정보를 스프링 시큐리티에 넘겨준다.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null){ // 해당 사용자가 없으면 로그인 페이지에서 에러 메시지를 출력하도록 설계됨
            throw new UsernameNotFoundException(email);
        }

        // 아래 부분이 스프링 시큐리티에 넘겨줄 사용자 정보 및 권한 정보 객체를 만드는 부분
        UserDetails userDetails = User.builder()
                .username(member.getEmail()) // 로그인을 이메일로 할 것임
                .password(member.getPassword())
                .roles(member.getMemberRole().toString()) // 자동으로 앞에 ROLE_을 붙여줌
                .build();

        return userDetails; // 사용자 정보, 권한 정보를 스프링 시큐리티에 넘겨줌
    }
}
