package com.knotted.repository;

import com.knotted.entity.Member;
import com.knotted.entity.Order;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByMember(Member member, Sort sort);
}
