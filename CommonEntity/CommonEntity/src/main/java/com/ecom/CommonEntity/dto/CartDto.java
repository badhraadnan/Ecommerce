package com.ecom.CommonEntity.dto;

import com.ecom.CommonEntity.entity.Cart;
import com.ecom.CommonEntity.entity.User;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartDto {

    private Long cartId;

    public Double totalAmount;

    private Long userId;

    private List<CartItemsDTO> cartItems;

    public static Cart toEntity(CartDto cartDto, User user){
        return Cart.builder()
                .user(user)
                .totalAmount(cartDto.getTotalAmount())
                .build();
    }

    public static CartDto toDto(Cart cart) {
        return CartDto.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getUserId())
                .totalAmount(cart.getTotalAmount())
                .cartItems(
                        cart.getCartItems() != null ?
                                cart.getCartItems().stream().map(CartItemsDTO::toDto).toList() : null
                )
                .build();
    }

}