package com.knotted.repository;

import com.knotted.entity.Item;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // 반환 타입이 List일 경우 List로 선언해줘야 함
    // 두 번째 인자로 Sort를 지정함
    List<Item> findByCategory(String category, Sort sort);
    
    // 검색어로 검색
    List<Item> findAllByNameContainingIgnoreCase(String searchWord, Sort sort);
    
    // 카테고리에다가 검색어도 포함
    List<Item> findByCategoryAndNameContainingIgnoreCase(String category, String searchWord, Sort sort);
}
