package com.knotted.service.admin;

import com.knotted.dto.MemberDTO;
import com.knotted.entity.Member;
import com.knotted.repository.admin.AdminMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminMemberService {

    private final AdminMemberRepository adminMemberRepository;

    public List<MemberDTO> getMembers(){
        List<Member> memberList = adminMemberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();

        for(Member member : memberList){
            MemberDTO memberDTO = MemberDTO.of(member);

            memberDTOList.add(memberDTO);
        }

        return memberDTOList;
    }
}
