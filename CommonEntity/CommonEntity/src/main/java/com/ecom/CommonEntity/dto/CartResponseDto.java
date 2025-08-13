package com.ecom.CommonEntity.dto;

import com.ecom.CommonEntity.entity.CartItem;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartResponseDto {

    private Long cartItemId;

    private Long cartId;

    private Long userId;

    private Long productId;

    private String productName;

    private String imageUrl;

    private Double price;

    private int quantity;

    public Double totalAmount;

    public static CartResponseDto responseDto(CartItem cartItem){
        return CartResponseDto.builder()
                .cartItemId(cartItem.getCartItemsId())
                .cartId(cartItem.getCart().getCartId())
                .totalAmount(cartItem.getCart().getTotalAmount())
                .userId(cartItem.getCart().getUser().getUserId())
                .productId(cartItem.getProduct().getProductID())
                .productName(cartItem.getProduct().getName())
                .imageUrl(cartItem.getProduct().getImageURL())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .build();
    }
}
