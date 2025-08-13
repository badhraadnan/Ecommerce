package com.ecom.commonRepository.repo;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<Category,Long> {
    Category findByName(String name);
    List<Category> findAllByStatus(Status status);

    Optional<Category> findBycatIDAndStatus(long id, Status status);


}
