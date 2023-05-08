package com.knotted.repository;

import com.knotted.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String email); // 회원가입 시 중복 검사용
}
