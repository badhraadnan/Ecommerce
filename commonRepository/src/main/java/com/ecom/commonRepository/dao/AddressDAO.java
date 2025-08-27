package com.ecom.commonRepository.dao;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.UserProfileDto;
import com.ecom.CommonEntity.entity.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AddressDAO {

    @Autowired
    private MasterDao masterDAO;

    public Address saveAddress(Address address){
        return masterDAO.getAddressRepo().save(address);
    }

    public List<Address> findAllByStatus(Status status){
        return masterDAO.getAddressRepo().findAllByStatus(status);
    }

    public Optional<Address> findByUserIdAndStatus(long userId, Status status){
        return masterDAO.getAddressRepo().findByUser_UserIdAndStatus(userId,status);
    }

    public Optional<Address> findByUser_UserIdAndAddressId(long userId, Long addressId){
        return masterDAO.getAddressRepo().findByUser_UserIdAndAddressId(userId, addressId);
    }

    public List<Object[]> findUsersAllDetailsByAddress(){
        return masterDAO.getAddressRepo().findUsersAllDetailsByAddress();
    }

    public List<UserProfileDto> findUser(){
        return masterDAO.getAddressRepo().findUser();
    }


    public List<Address> findUserByIdAndStatus(Long userId,Status status){
        return masterDAO.getAddressRepo().findByUserIdAndUserStatus(userId,status);
    }
}
