package com.knotted.repository;

import com.knotted.entity.Favorite;
import com.knotted.entity.Item;
import com.knotted.entity.Member;
import com.knotted.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // 회원, 매장으로 즐겨찾기 찾기
    Favorite findByMemberAndStore(Member member, Store store);

    // 회원, 상품으로 즐겨찾기 찾기
    Favorite findByMemberAndItem(Member member, Item item);

}
