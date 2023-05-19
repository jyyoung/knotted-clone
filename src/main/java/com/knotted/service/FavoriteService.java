package com.knotted.service;


import com.knotted.constant.FavoriteType;
import com.knotted.dto.ItemDTO;
import com.knotted.dto.StoreDTO;
import com.knotted.dto.StoreImageDTO;
import com.knotted.entity.*;
import com.knotted.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final StoreImageRepository storeImageRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;

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

    // 해당 회원의 즐겨찾기한 상품 조회하는 메소드
    public List<ItemDTO> getFavoriteItems(String memberEmail){
        Member member = memberRepository.findByEmail(memberEmail);

        List<Favorite> favoriteList = favoriteRepository.findByMemberAndFavoriteType(member, FavoriteType.ITEM);

        List<ItemDTO> itemDTOList = new ArrayList<>();

        for(Favorite favorite : favoriteList){
            Item item = favorite.getItem();
            ItemDTO itemDTO = itemService.convertToItemDTO(item);
            itemDTOList.add(itemDTO);
        }

        return itemDTOList;
    }

    // 해당 회원의 즐겨찾기한 매장 조회하는 메소드
    public List<StoreDTO> getFavoriteStores(String memberEmail){
        Member member = memberRepository.findByEmail(memberEmail);

        List<Favorite> favoriteList = favoriteRepository.findByMemberAndFavoriteType(member, FavoriteType.STORE);

        List<StoreDTO> storeDTOList = new ArrayList<>();

        for(Favorite favorite : favoriteList){
            Store store = favorite.getStore();
            StoreDTO storeDTO = StoreDTO.of(store);
            StoreImage storeImage = storeImageRepository.findByStoreId(store.getId()); // 매장 이미지 엔티티 조회

            if(storeImage != null){ // 해당 이미지가 있으면
                StoreImageDTO storeImageDTO = StoreImageDTO.of(storeImage);
                storeDTO.setStoreImageDTO(storeImageDTO); // 매장 이미지 DTO를 매장 DTO에 세팅
            }

            storeDTOList.add(storeDTO);
        }

        return storeDTOList;
    }

}
