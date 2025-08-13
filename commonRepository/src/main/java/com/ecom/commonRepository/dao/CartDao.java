package com.ecom.commonRepository.dao;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entity.Cart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CartDao {

    @Autowired
    private MasterDao masterDao;

    public Cart saveCart(Cart cart) {
        return masterDao.getCartRepo().save(cart);
    }

    public Optional<Cart> findById(Long cartId) {
        return masterDao.getCartRepo().findById(cartId);
    }

    public Optional<Cart> findByUserIdAndStatus(Long userId, Status status) {
        return masterDao.getCartRepo().findByUserIdAndStatus(userId, status);
    }

    //    public List<Cart> findByUserId(Long userId, Status status){
    //        return masterDao.getCartRepo().findByUserId(userId,status);
    //    }

    public void deleteByIdCart(Long cartId) {
        masterDao.getCartRepo().deleteById(cartId);
    }
}