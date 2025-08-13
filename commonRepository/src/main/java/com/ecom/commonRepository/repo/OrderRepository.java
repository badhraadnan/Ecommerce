package com.ecom.commonRepository.repo;

import com.ecom.CommonEntity.dto.OrderDto;
import com.ecom.CommonEntity.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {
    List<Orders> findByUser_UserId(Long userId);

//    @Query("""
//  SELECT new com.ecom.CommonEntity.dto.OrderDto(
//    o.orderId,
//    u.userId,
//    u.firstName,
//    u.email,
//    u.mobile,
//    a.shortAddress,
//    c.countryName,
//    s.stateName,
//    ct.cityName,
//    a.zipCode,
//    p.productID,
//    p.name,
//    p.description,
//    p.price,
//    p.imageURL,
//    o.qty
//  )
//  FROM Orders o
//  JOIN o.user u
//  JOIN o.product p
//  JOIN Address a ON a.user.userId = u.userId
//  JOIN a.country c
//  JOIN a.state s
//  JOIN a.city ct
//  WHERE o.user.userId = :userId
//""")
//    List<OrderDto> getUserOrderDetails(@Param("userId") Long userId);






}