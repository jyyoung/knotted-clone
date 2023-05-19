package com.knotted.repository;

import com.knotted.entity.Order;
import com.knotted.entity.RewardHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RewardHistoryRepository extends JpaRepository<RewardHistory, Long> {

    RewardHistory findByOrderAndType(Order order, boolean type);
}
