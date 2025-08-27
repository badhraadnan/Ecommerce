package com.ecom.productservice.controller;


import com.ecom.CommonEntity.dto.CartDto;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.productservice.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/product/service/cart")
public class CartController {

    @Autowired
    private CartItemService cartItemService;

    @PostMapping("/add")
    public ResponseModel addToCart(@RequestBody CartDto cartDto) {
        return cartItemService.addToCart(cartDto);
    }

    @GetMapping("/{userId}")
    public ResponseModel getCartByUserId(@PathVariable Long userId) {
        return cartItemService.getCartByUserId(userId);
    }

    @DeleteMapping("/remove/{cartId}")
    public ResponseModel removeCartItem(@PathVariable Long cartId) {
        return cartItemService.removeCartItem(cartId);
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseModel clearCart(@PathVariable Long userId) {
        return cartItemService.clearCartByUserId(userId);
    }

    @PutMapping("/")
    public ResponseModel updateQuantity(@RequestParam Long cartItemId, @RequestParam int quantity) {
        return cartItemService.updateQuantity(cartItemId, quantity);
    }

}
