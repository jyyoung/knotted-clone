package com.knotted.repository;

import com.knotted.entity.Cart;
import com.knotted.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByMember(Member member);
}
