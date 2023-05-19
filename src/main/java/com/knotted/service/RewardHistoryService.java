package com.knotted.service;

import com.knotted.dto.RewardHistoryDTO;
import com.knotted.dto.StoreDTO;
import com.knotted.entity.Member;
import com.knotted.entity.Order;
import com.knotted.entity.RewardHistory;
import com.knotted.entity.Store;
import com.knotted.repository.OrderRepository;
import com.knotted.repository.RewardHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RewardHistoryService {

    private final RewardHistoryRepository rewardHistoryRepository;
    private final OrderRepository orderRepository;
    
    // 회원 엔티티로 획득한 적립금 내역 조회
    public List<RewardHistoryDTO> getAcquireRewardHistoryByMember(Member member){
        List<RewardHistory> rewardHistoryList = rewardHistoryRepository.findByMemberAndType(member, false, Sort.by(Sort.Direction.DESC, "id"));

        List<RewardHistoryDTO> rewardHistoryDTOList = new ArrayList<>();

        for(RewardHistory rewardHistory : rewardHistoryList){
            RewardHistoryDTO rewardHistoryDTO = RewardHistoryDTO.of(rewardHistory);
            
            // 매장 정보도 넣음
            Store store = rewardHistory.getStore();
            StoreDTO storeDTO = StoreDTO.of(store);
            rewardHistoryDTO.setStoreDTO(storeDTO);

            rewardHistoryDTOList.add(rewardHistoryDTO);
        }

        return rewardHistoryDTOList;
    }
    
    // 회원 엔티티로 사용한 적립금 내역 조회
    public List<RewardHistoryDTO> getUseRewardHistoryByMember(Member member){
        List<RewardHistory> rewardHistoryList = rewardHistoryRepository.findByMemberAndType(member, true, Sort.by(Sort.Direction.DESC, "id"));

        List<RewardHistoryDTO> rewardHistoryDTOList = new ArrayList<>();

        for(RewardHistory rewardHistory : rewardHistoryList){
            RewardHistoryDTO rewardHistoryDTO = RewardHistoryDTO.of(rewardHistory);

            // 매장 정보도 넣음
            Store store = rewardHistory.getStore();
            StoreDTO storeDTO = StoreDTO.of(store);
            rewardHistoryDTO.setStoreDTO(storeDTO);

            rewardHistoryDTOList.add(rewardHistoryDTO);
        }

        return rewardHistoryDTOList;
    }

    // 주문 ID로 사용한 적립금 내역 조회
    public RewardHistoryDTO getUseRewardHistory(Long orderId){
        Order order = orderRepository.findById(orderId)
                .orElseThrow(EntityNotFoundException::new);

        // 사용한 적립금 조회
        RewardHistory rewardHistory = rewardHistoryRepository.findByOrderAndType(order, true);

        RewardHistoryDTO rewardHistoryDTO = new RewardHistoryDTO();

        if(rewardHistory != null){ // 사용한 적립금이 있을 때만
            rewardHistoryDTO = RewardHistoryDTO.of(rewardHistory);
        }

        return rewardHistoryDTO;
    }

}
