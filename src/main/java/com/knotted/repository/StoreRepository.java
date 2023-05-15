package com.knotted.repository;

import com.knotted.entity.Store;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long> {

    // 검색어로 검색
    List<Store> findAllByNameContainingIgnoreCase(String searchWord, Sort sort);
}
