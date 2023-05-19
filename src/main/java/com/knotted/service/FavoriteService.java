package com.knotted.service;


import com.knotted.constant.FavoriteType;
import com.knotted.entity.Favorite;
import com.knotted.entity.Item;
import com.knotted.entity.Member;
import com.knotted.entity.Store;
import com.knotted.repository.FavoriteRepository;
import com.knotted.repository.ItemRepository;
import com.knotted.repository.MemberRepository;
import com.knotted.repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final ItemRepository itemRepository;

    // 해당 사용자의 매장 즐겨찾기가 있는지 확인
    public boolean storeFavoriteExists(String memberEmail, Long storeId){
        // 존재하면 true, 아니면 false 반환

        Member member = memberRepository.findByEmail(memberEmail);
        Store store = storeRepository.findById(storeId)
                .orElseThrow(EntityNotFoundException::new);

        Favorite favorite = favoriteRepository.findByMemberAndStore(member, store);

        if(favorite != null){
            return true;
        }else{
            return false;
        }
    }

    // 해당 사용자의 상품 즐겨찾기가 있는지 확인
    public boolean itemFavoriteExists(String memberEmail, Long itemId){
        // 존재하면 true, 아니면 false 반환

        Member member = memberRepository.findByEmail(memberEmail);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        Favorite favorite = favoriteRepository.findByMemberAndItem(member, item);

        if(favorite != null){
            return true;
        }else{
            return false;
        }
    }


    // 매장 즐겨찾기 토글
    public boolean toggleStoreFavorite(Long storeId, String memberEmail){

        Member member = memberRepository.findByEmail(memberEmail);
        Store store = storeRepository.findById(storeId)
                .orElseThrow(EntityNotFoundException::new);

        // 현재 조건으로 즐겨찾기가 존재하는지 확인한다
        Favorite savedFavorite = favoriteRepository.findByMemberAndStore(member, store);

        if(savedFavorite == null){ // 없으면 추가한다

            Favorite favorite = new Favorite();
            favorite.setMember(member);
            favorite.setStore(store);
            favorite.setFavoriteType(FavoriteType.STORE);

            favoriteRepository.save(favorite);

            return true;

        }else{ // 있으면 삭제한다
            favoriteRepository.delete(savedFavorite);

            return false;
        }
    }
    
    // 상품 즐겨찾기 토글
    public boolean toggleItemFavorite(Long itemId, String memberEmail){

        Member member = memberRepository.findByEmail(memberEmail);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // 현재 조건으로 즐겨찾기가 존재하는지 확인한다
        Favorite savedFavorite = favoriteRepository.findByMemberAndItem(member, item);

        if(savedFavorite == null){ // 없으면 추가한다

            Favorite favorite = new Favorite();
            favorite.setMember(member);
            favorite.setItem(item);
            favorite.setFavoriteType(FavoriteType.ITEM);

            favoriteRepository.save(favorite);

            return true;

        }else{ // 있으면 삭제한다
            favoriteRepository.delete(savedFavorite);

            return false;
        }
    }

}
