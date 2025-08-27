package com.ecom.commonRepository.repo;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("Select u from User as u where u.userId=?1 And u.status=?2")
    Optional<User> findByUserIdAndStatus(long id, Status status);

    @Query("Select u from User as u where u.email=?1 And u.status=?2")
    Optional<User> findByEmailAndStatus(String email, Status status);

    Optional<User> findByEmail(String email);

    List<User> findAllByStatus(Status status);


    Optional<User> findByEmailOrMobile(String email,String mobile);

//    @Modifying
//    @Transactional
//    @Query("DELETE FROM User u WHERE u.email = :email")
//    void deleteUser(@Param("email") String email);


    Optional<User> findByEmailAndPasswordAndStatus(String email,String password,Status status);
}
