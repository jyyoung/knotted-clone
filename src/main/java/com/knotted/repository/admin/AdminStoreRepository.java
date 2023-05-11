package com.knotted.repository.admin;

import com.knotted.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminStoreRepository extends JpaRepository<Store, Long> {
}
