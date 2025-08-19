package com.ecom.CommonEntity.dto;


import com.ecom.CommonEntity.entity.Cart;
import com.ecom.CommonEntity.entity.Product;
import com.ecom.CommonEntity.entity.CartItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CartItemsDTO implements Serializable {

    private Long cartItemId;
    private Long cartId;
    private Long productId;
    private String productName;
    private String imageUrl;
    private Double price;
    private int quantity;

    public static CartItem toEntity(CartItemsDTO dto, Cart cart, Product product) {
        return CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(dto.getQuantity())
                .price(product.getPrice()) // Optionally override this with dto.getPrice()
                .build();
    }

    public static CartItemsDTO toDto(CartItem cartItem) {
        return CartItemsDTO.builder()
                .cartItemId(cartItem.getCartItemsId())
                .cartId(cartItem.getCart().getCartId())
                .productId(cartItem.getProduct().getProductID())
                .productName(cartItem.getProduct().getName())
                .imageUrl(cartItem.getProduct().getImageURL())
                .price(cartItem.getPrice())
                .quantity(cartItem.getQuantity())
                .build();
    }
}
