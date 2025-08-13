package com.ecom.commonRepository.dao;

import com.ecom.CommonEntity.entity.CartItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CartItemsDao {

    @Autowired
    private MasterDao masterDao;

    public CartItem saveCartItems(CartItem cartItems) {
        return masterDao.getCartItemsRepo().save(cartItems);
    }

    public List<CartItem> findByUserId(Long userId) {
        return masterDao.getCartItemsRepo().findByUserId(userId);
    }

    public List<CartItem> findByCartId(Long cartId) {
        return masterDao.getCartItemsRepo().findByCart_CartId(cartId);
    }

    public Optional<CartItem> findByUserIdAndProductIdCartItems(Long userId, Long productId) {
        return masterDao.getCartItemsRepo().findByUserIdAndProductId(userId, productId);
    }

    public Optional<CartItem> findByIdCartItems(Long cartItemsId) {
        return masterDao.getCartItemsRepo().findById(cartItemsId);
    }

    public void deleteByIdCartItems(Long cartItemsId) {
        masterDao.getCartItemsRepo().deleteById(cartItemsId);
    }

    public void deleteAllCartItems(List<CartItem> cartItemsId) {
        masterDao.getCartItemsRepo().deleteAll(cartItemsId);
    }
}
