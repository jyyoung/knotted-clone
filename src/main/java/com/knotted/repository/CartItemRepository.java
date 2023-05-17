package com.knotted.repository;

import com.knotted.entity.Cart;
import com.knotted.entity.CartItem;
import com.knotted.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndItem(Cart cart, Item item);
    List<CartItem> findAllByCart(Cart cart);


}
