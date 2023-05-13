package com.knotted.repository.admin;

import com.knotted.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminItemImageRepository extends JpaRepository<Item, Long> {

}
