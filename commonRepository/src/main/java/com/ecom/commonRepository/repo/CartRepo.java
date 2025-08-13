package com.ecom.commonRepository.repo;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.cartReminderDto;
import com.ecom.CommonEntity.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepo extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.userId = :userId")
    List<Cart> getAllCart(@Param("userId") Long userId);


    @Query("SELECT c FROM Cart c WHERE c.user.userId = :userId")
    Optional<Cart> getCartByUserId(@Param("userId") Long userId);


    @Query("SELECT c FROM Cart c JOIN c.user u ")
    List<Cart> getAllCartByUserId();


    @Query("SELECT c FROM Cart c WHERE c.user.userId = :userId AND c.user.status = :status")
    Optional<Cart> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Status status);


    void deleteByUser_UserId(Long userId);



}