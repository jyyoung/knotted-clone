package com.knotted.repository.admin;

import com.knotted.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminItemRepository extends JpaRepository<Item, Long> {

}
