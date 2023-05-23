package com.knotted.controller.admin;

import com.knotted.dto.MemberDTO;
import com.knotted.repository.admin.AdminMemberRepository;
import com.knotted.service.admin.AdminMemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/admin/member")
@Controller
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberRepository adminMemberRepository;
    private final AdminMemberService adminMemberService;
    
    // 회원 관리 메인 (목록 조회)
    @GetMapping(value = {"", "/"})
    public String main(Model model){
        List<MemberDTO> memberDTOList = adminMemberService.getMembers();

        model.addAttribute("memberDTOList", memberDTOList);

        return "admin/member/index";
    }

}
