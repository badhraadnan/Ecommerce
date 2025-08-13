package com.ecom.commonRepository.repo;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entity.OrderItem;
import com.ecom.CommonEntity.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface orderItemsRepo extends JpaRepository<OrderItem,Long> {

    @Query("Select o from OrderItem o where o.orders.user.userId = :userId And o.orders.user.status = :status")
    List<OrderItem> findUserIdAndStatus(@Param("userId") Long userId,@Param("status") Status status);
}
