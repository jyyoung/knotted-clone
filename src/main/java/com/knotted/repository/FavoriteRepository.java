package com.knotted.repository;

import com.knotted.constant.FavoriteType;
import com.knotted.entity.Favorite;
import com.knotted.entity.Item;
import com.knotted.entity.Member;
import com.knotted.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    // 회원, 매장으로 즐겨찾기 찾기
    Favorite findByMemberAndStore(Member member, Store store);

    // 회원, 상품으로 즐겨찾기 찾기
    Favorite findByMemberAndItem(Member member, Item item);

    // 회원으로 상품 즐겨찾기 조회
    List<Favorite> findByMemberAndFavoriteType(Member member, FavoriteType favoriteType);

}
