package com.knotted.repository;

import com.knotted.entity.Member;
import com.knotted.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
