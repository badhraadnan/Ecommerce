package com.ecom.commonRepository.repo;
import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.UserProfileDto;
import com.ecom.CommonEntity.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address,Long> {
    List<Address> findAllByStatus(Status status);
    Optional<Address> findByUser_UserIdAndAddressId(long userId, Long addressId);


    Optional<Address> findByUser_UserIdAndStatus(long userId, Status status);

    @Query("SELECT a FROM Address a WHERE a.user.email = :email AND a.user.status = :status")
    List<Address> findByUserEmailAndUserStatus(@Param("email") String email,
                                                   @Param("status") Status status);
    //With SQL

    @Query(nativeQuery = true,value = "SELECT " +
            "u.first_name,u.email,a.short_address, c.name,s.name,ct.name,a.zip_code " +
            "FROM user AS u " +
            "JOIN address AS a ON u.user_id = a.user_id " +
            "JOIN country AS c ON a.country_id = c.country_id " +
            "JOIN state AS s ON a.state_id = s.state_id " +
            "JOIN city AS ct ON a.city_id = ct.city_id " +
            "WHERE u.status='ACTIVE' And a.status='ACTIVE' ")
    List<Object[]> findUsersAllDetailsByAddress();


    //With JPQL

    @Query("SELECT new com.ecom.CommonEntity.dto.UserProfileDto(" +
            "u.firstName, u.email, u.mobile ,a.shortAddress, c.countryName, s.stateName, ct.cityName, a.zipCode) " +
            "FROM User u " +
            "JOIN u.address a " +
            "JOIN a.country c " +
            "JOIN a.state s " +
            "JOIN a.city ct " +
            "WHERE u.status = 'ACTIVE' AND a.status = 'ACTIVE'")
    List<UserProfileDto> findUser();

}