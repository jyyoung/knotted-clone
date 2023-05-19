package com.knotted.repository;

import com.knotted.entity.Member;
import com.knotted.entity.Order;
import com.knotted.entity.RewardHistory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardHistoryRepository extends JpaRepository<RewardHistory, Long> {

    RewardHistory findByOrderAndType(Order order, boolean type);

    List<RewardHistory> findByMemberAndType(Member member, boolean type, Sort sort);
}
