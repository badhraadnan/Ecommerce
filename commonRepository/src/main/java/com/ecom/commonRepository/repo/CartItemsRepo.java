package com.ecom.commonRepository.repo;

import com.ecom.CommonEntity.entity.Cart;
import com.ecom.CommonEntity.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemsRepo extends JpaRepository<CartItem , Long> {

    @Query("Select ci from CartItem ci " +
            "Join ci.cart c " +
            "Where c.user.userId = :userId ")
    List<CartItem> getAllCartByUser(@Param("userId") Long userId);

    @Query("Select c from CartItem c where c.cart.cartId = :cartId")
    List<CartItem> findCartByCartId(@Param("cartId") Long cartId);


    List<CartItem> findByCart_CartId(Long cartId);


    @Query("SELECT ci FROM CartItem ci " +
            "JOIN ci.cart c " +
            "JOIN c.user u " +
            "WHERE c.user.userId = :userId")
    List<CartItem> findByUserId(@Param("userId") Long userId);

    @Query("SELECT ci FROM CartItem ci " +
            "JOIN ci.cart c " +
            "JOIN c.user u " +
            "WHERE c.user.userId = :userId AND ci.product.productID = :productId")
    Optional<CartItem> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}
