package com.knotted.repository.admin;

import com.knotted.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminOrderRepository extends JpaRepository<Order, Long> {

}
