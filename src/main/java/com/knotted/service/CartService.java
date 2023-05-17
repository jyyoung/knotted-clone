package com.knotted.service;

import com.knotted.entity.Cart;
import com.knotted.entity.Member;
import com.knotted.repository.CartRepository;
import com.knotted.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;

    // 회원 이메일 및 매장 ID로 Cart 찾는다. 있으면 해당 Cart의 매장 ID와 비교한다
    // 장바구니가 없거나 해당 매장 ID와 같은 경우 true 반환, 아니면 false 반환
    public boolean storeCheck(String memberEmail, Long storeId){
        Member member = memberRepository.findByEmail(memberEmail);

        Cart cart = cartRepository.findByMember(member);

        if(cart == null){
            // 아직 장바구니가 없는 경우 true 반환
            return true;
        }else{
            // 장바구니가 있는 경우 해당 cart의 store와 같은지 확인하고, 다르면 false를 반환한다
            if(cart.getStore().getId() == storeId){
                return true;
            }else{
                return false;
            }
        }
    }

    // 장바구니 생성 및 장바구니 상품 추가
    // 잘못되면 exception 던질 거라 void로 하겠음
    public void addToCart(String memberEmail, Long storeId, Long itemId, LocalDateTime reserveDate, Long count){
        Member member = memberRepository.findByEmail(memberEmail);
        Long memberId = member.getId();

        // 장바구니 생성
        Cart cart = new Cart();

        // 장바구니 상품 추가
    }

}
