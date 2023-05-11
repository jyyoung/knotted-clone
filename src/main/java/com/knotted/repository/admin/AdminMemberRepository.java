package com.knotted.repository.admin;

import com.knotted.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminMemberRepository extends JpaRepository<Member, Long> {

}
