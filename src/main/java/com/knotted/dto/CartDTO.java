package com.knotted.dto;

import com.knotted.entity.Cart;
import lombok.Data;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CartDTO {

    // 장바구니 ID
    private Long id;

    // 회원 DTO
    private MemberDTO memberDTO;

    // 매장 DTO
    private StoreDTO storeDTO;

    // 예약일자
    private LocalDateTime reserveDate;

    // 장바구니 상품 DTO 리스트
    private List<CartItemDTO> cartItemDTOList;

    // 장바구니 상품 총 금액
    private Long totalPrice;

    // 엔티티 <-> DTO간 변환에 사용할 ModelMapper 객체
    private static ModelMapper modelMapper = new ModelMapper();

    // 엔티티 -> DTO 변환
    public static CartDTO of(Cart cart){
        return modelMapper.map(cart, CartDTO.class);
    }

    // DTO -> 엔티티 변환
    public Cart createCart(){
        return modelMapper.map(this, Cart.class);
    }

}
