package com.ecom.ProductService.service.Impl;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.CartDto;
import com.ecom.CommonEntity.dto.CartItemsDTO;
import com.ecom.CommonEntity.entity.Cart;
import com.ecom.CommonEntity.entity.CartItem;
import com.ecom.CommonEntity.entity.Product;
import com.ecom.CommonEntity.entity.User;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.ProductService.service.CartItemService;
import com.ecom.CommonEntity.dto.CartResponseDto;
import com.ecom.commonRepository.dao.CartDao;
import com.ecom.commonRepository.dao.CartItemsDao;
import com.ecom.commonRepository.dao.MasterDao;
import com.ecom.commonRepository.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {


    @Autowired
    private MasterDao masterDao;

    @Autowired
    private CartDao cartDao;

    @Autowired
    private CartItemsDao cartItemDao;

    @Autowired
    private UserDao userDao;

    // addToCart
    @Override
    public ResponseModel addToCart(CartDto cartDto) {
        try {
            User user = masterDao.getUserRepository()
                    .findByUserIdAndStatus(cartDto.getUserId(), Status.ACTIVE)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Cart cart = cartDao.findByUserIdAndStatus(user.getUserId(), Status.ACTIVE)
                    .orElseGet(() -> {
                        Cart newCart = Cart.builder()
                                .user(user)
                                .totalAmount(0.0)
                                .build();
                        return cartDao.saveCart(newCart);
                    });

            Double totalAddedAmount = 0.0;
            CartItem lastProcessedItem = null;

            for (CartItemsDTO itemDto : cartDto.getCartItems()) {
                Product product = masterDao.getProductRepo()
                        .findByproductIDAndStatus(itemDto.getProductId(), Status.ACTIVE)
                        .orElseThrow(() -> new RuntimeException("Product not found: ID " + itemDto.getProductId()));

                double unitPrice = product.getPrice();
                int quantityToAdd = itemDto.getQuantity();
                double incomingTotalPrice = unitPrice * quantityToAdd;

                Optional<CartItem> existingItemOpt = cartItemDao
                        .findByUserIdAndProductIdCartItems(user.getUserId(), itemDto.getProductId());

                CartItem cartItem;
                if (existingItemOpt.isPresent()) {
                    cartItem = existingItemOpt.get();
                    int updatedQuantity = cartItem.getQuantity() + quantityToAdd;
                    double oldTotal = cartItem.getPrice();
                    double newTotal = unitPrice * updatedQuantity;

                    cartItem.setQuantity(updatedQuantity);
                    cartItem.setPrice(newTotal);

                    totalAddedAmount += (newTotal - oldTotal);
                } else {
                    cartItem = CartItemsDTO.toEntity(itemDto, cart, product);
                    cartItem.setPrice(incomingTotalPrice);
                    totalAddedAmount += incomingTotalPrice;
                }

                cartItemDao.saveCartItems(cartItem);
                lastProcessedItem = cartItem;
            }

            cart.setTotalAmount(cart.getTotalAmount() + totalAddedAmount);
            cartDao.saveCart(cart);

            return new ResponseModel(HttpStatus.OK,
                    CartResponseDto.responseDto(lastProcessedItem), "Item(s) added to cart");

        } catch (RuntimeException e) {
            return new ResponseModel(
                    HttpStatus.NOT_FOUND,
                    null,
                    e.getMessage()
            );
        }catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "Something went wrong: " + e.getMessage());
        }
    }



    @Override
    public ResponseModel getCartByUserId(Long userId) {
        try {
            Optional<User> user = masterDao.getUserRepository().findByUserIdAndStatus(userId, Status.ACTIVE);

            if (user.isEmpty()) {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "User not found");
            }

            List<CartItem> cartList = cartItemDao.findByUserId(userId);

            if (cartList.isEmpty()) {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "Cart not found");
            }

            List<CartResponseDto> response = cartList.stream()
                    .map(CartResponseDto::responseDto)
                    .toList();

            return new ResponseModel(HttpStatus.OK, response, "Cart items retrieved");

        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Error: " + e.getMessage());
        }
    }


    //Remove CartItems From Cart
    @Override
    public ResponseModel removeCartItem(Long cartItemId) {
        try {
            // Find the cart item
            Optional<CartItem> cartItemOpt = cartItemDao.findByIdCartItems(cartItemId);

            if (cartItemOpt.isEmpty()) {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "Cart item not found");
            }

            CartItem cartItem = cartItemOpt.get();
            Cart cart = cartItem.getCart();

            if (cart == null || cartDao.findById(cart.getCartId()).isEmpty()) {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "Cart not found");
            }

            Double newTotal = cart.getTotalAmount() - cartItem.getPrice();
            cart.setTotalAmount(newTotal);
            cartDao.saveCart(cart);

            // Remove item
            cartItemDao.deleteByIdCartItems(cartItem.getCartItemsId());

            return new ResponseModel(HttpStatus.OK, null, "Item removed from cart");

        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Error: " + e.getMessage());
        }
    }

    //Remove All Cart Items
    @Override
    public ResponseModel clearCartByUserId(Long userId) {
        try {
            Optional<User> user = masterDao.getUserRepository().findByUserIdAndStatus(userId, Status.ACTIVE);
            if (user.isEmpty()) {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "User not found");
            }

            Optional<Cart> cartOpt = cartDao.findByUserIdAndStatus(userId, Status.ACTIVE);
            if (cartOpt.isEmpty()) {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "Cart not found");
            }

            Cart cart = cartOpt.get();
            List<CartItem> userCart = cartItemDao.findByCartId(cart.getCartId());

            if (userCart.isEmpty()) {
                return new ResponseModel(HttpStatus.NO_CONTENT, null, "Cart already empty");
            }

            cartItemDao.deleteAllCartItems(userCart);
            cartDao.deleteByIdCart(cart.getCartId());

            return new ResponseModel(HttpStatus.OK, null, "Cart cleared successfully");

        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Error: " + e.getMessage());
        }
    }

    //Update Quantity
    @Override
    public ResponseModel updateQuantity(Long cartItemId, int quantity) {
        try {
            if (quantity <= 0) {
                return new ResponseModel(HttpStatus.BAD_REQUEST, null, "Quantity must be greater than 0");
            }

            Optional<CartItem> cartItemOpt = cartItemDao.findByIdCartItems(cartItemId);
            if (cartItemOpt.isEmpty()) {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "Cart item not found");
            }

            CartItem cartItem = cartItemOpt.get();
            Cart cart = cartItem.getCart();

            if (cart == null || cartDao.findById(cart.getCartId()).isEmpty()) {
                return new ResponseModel(HttpStatus.NOT_FOUND, null, "Cart not found");
            }

            double unitPrice = cartItem.getProduct().getPrice();
            double oldItemPrice = cartItem.getPrice();
            double newItemPrice = unitPrice * quantity;

            // Update item
            cartItem.setQuantity(quantity);
            cartItem.setPrice(newItemPrice);

            // Update cart total
            double updatedCartTotal = (cart.getTotalAmount() - oldItemPrice) + newItemPrice;
            cart.setTotalAmount(Math.max(updatedCartTotal,0.0));

            // Save updates
            cartDao.saveCart(cart);
            CartItem updatedItem = cartItemDao.saveCartItems(cartItem);

            return new ResponseModel(HttpStatus.OK, CartResponseDto.responseDto(updatedItem), "Item quantity updated");

        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "Error: " + e.getMessage());
        }
    }
}