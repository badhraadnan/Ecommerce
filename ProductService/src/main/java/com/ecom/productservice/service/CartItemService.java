package com.ecom.productservice.service;

import com.ecom.CommonEntity.dto.CartDto;
import com.ecom.CommonEntity.model.ResponseModel;



public interface CartItemService {

    ResponseModel addToCart(CartDto cartDto);

    ResponseModel getCartByUserId(Long userId);

    ResponseModel removeCartItem(Long cartId);

    ResponseModel clearCartByUserId(Long userId);

    ResponseModel updateQuantity(Long cartId, int quantity);

}