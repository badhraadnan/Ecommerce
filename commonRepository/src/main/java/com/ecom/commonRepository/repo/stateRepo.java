package com.ecom.commonRepository.repo;


import com.ecom.CommonEntity.entity.State;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface stateRepo extends JpaRepository<State,Long> {
    
    @Query(nativeQuery = true, value =
            "SELECT * from state WHERE country_id = ?1")
    List<State> listOfStateCountryWise(int id);

}
