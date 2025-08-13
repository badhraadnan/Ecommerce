package com.ecom.commonRepository.repo;

import com.ecom.CommonEntity.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface countryRepo extends JpaRepository<Country,Long> {
    List<Country> findAllByCountryId(int id);
}
