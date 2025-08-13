package com.ecom.commonRepository.dao;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDao {

    @Autowired
    private MasterDao masterDao;

    public Optional<User> findByUserIdAndStatus(long id, Status status){
        return masterDao.getUserRepository().findByUserIdAndStatus(id,status);
    }

    public User saveUser(User user){
        return masterDao.getUserRepository().save(user);
    }

    public List<User> getUser(){
        return masterDao.getUserRepository().findAll();
    }

    public List<User> userFindAllByStatus(Status status){
        return masterDao.getUserRepository().findAllByStatus(status);
    }

    public Optional<User> userFindByIdAndStatus(long id, Status status){
        return masterDao.getUserRepository().findByUserIdAndStatus(id,status);
    }

    public Optional<User> userFindById(long id){
        return masterDao.getUserRepository().findById(id);
    }

    public void deleteUser(String email){
        masterDao.getUserRepository().deleteUser(email);
    }

    public Optional<User> findByEmailORMobile(String email,String mobile){
        return masterDao.getUserRepository().findByEmailOrMobile(email,mobile);
    }

    public Optional<User> UserLoginByEmailAndPassword(String email,String password,Status status){
        return masterDao.getUserRepository().findByEmailAndPasswordAndStatus(email, password, status);
    }

    public Optional<User> findByEmail(String email){
        return masterDao.getUserRepository().findByEmail(email);
    }

    public Optional<User> findByEmailAndStatus(String email, Status status){
        return masterDao.getUserRepository().findByEmailAndStatus(email,status);
    }
}
