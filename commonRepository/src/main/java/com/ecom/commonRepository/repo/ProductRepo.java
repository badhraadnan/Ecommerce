package com.ecom.commonRepository.repo;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.productFeedDto;
import com.ecom.CommonEntity.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findAllByStatus(Status status);

    Optional<Product> findByproductIDAndStatus(long id, Status status);

    @Query(value = "SELECT p.productid AS productId, p.name AS productName, " +
            "c.catid AS catId, c.name AS categoryName, " +
            "p.price, p.qty, p.imageurl AS imageURL " +
            "FROM product AS p " +
            "JOIN category AS c ON p.cat_id = c.catid " +
            "WHERE p.status = 'ACTIVE' AND c.status = 'ACTIVE' " +
            "ORDER BY p.productid ASC " +
            "LIMIT :page, :size",
            nativeQuery = true)
    List<productFeedDto> getProductFeed(@Param("page") int page, @Param("size") int size);

    @Query(value = "SELECT p.productid AS productId, p.name AS productName, " +
            "c.catid , c.name , " +
            "p.price, p.qty, p.imageurl  " +
            "FROM product AS p " +
            "JOIN category AS c ON p.cat_id = c.catid " +
            "WHERE p.status = 'ACTIVE' AND c.status = 'ACTIVE' AND p.cat_id = ?1 " ,
            nativeQuery = true)
    List<Object[]> ProductFilterByCategory(int id);

    @Query(value = "SELECT p.productid, p.name, " +
            "p.price, p.qty, p.imageurl " +
            "FROM product AS p " +
            "WHERE p.status = 'ACTIVE'  AND p.name LIKE %?1%",
            nativeQuery = true)
    List<Object[]> filterByProduct(String input);




}