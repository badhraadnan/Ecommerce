package com.ecom.commonRepository.repo;


import com.ecom.CommonEntity.entity.Address;
import com.ecom.CommonEntity.entity.City;
import com.ecom.CommonEntity.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
//public interface cityRepo extends JpaRepository<City,Long> {
//}

public interface cityRepo extends JpaRepository<City,Long> {

    @Query(nativeQuery = true, value =
            "SELECT * From city WHERE state_id = ?1")
    List<City> listOfCityStateWise(int id);

}
