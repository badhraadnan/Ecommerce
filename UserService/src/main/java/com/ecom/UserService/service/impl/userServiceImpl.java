package com.ecom.UserService.service.impl;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.UserDto;
import com.ecom.CommonEntity.entity.User;
import com.ecom.CommonEntity.model.ErrorMsg;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.CommonEntity.model.SuccessMsg;
import com.ecom.UserService.exception.UserNotFoundException;
import com.ecom.UserService.service.UserService;
import com.ecom.commonRepository.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class userServiceImpl implements UserService {

    @Autowired
    private UserDao userDAO;


    //Get All Users  --Admin User
    @Override
    public ResponseModel getAllUsers() {
        List<User> user = userDAO.userFindAllByStatus(Status.ACTIVE);

        if (user != null) {
            List<UserDto> dto = user.stream()
                    .map(UserDto::toDto)
                    .toList();

            return new ResponseModel(HttpStatus.OK, dto, SuccessMsg.USER_GET_SUCCESS);
        }
        return null;
    }


    //Update User  --User Side
    @Override
    public ResponseModel updateUser(UserDto userDto) {
        try {
            User existUser = userDAO.userFindByIdAndStatus(userDto.getUserId(), Status.ACTIVE)
                    .orElseThrow(() -> new UserNotFoundException(ErrorMsg.USER_NOT_FOUND));


            UserDto.updateUser(userDto, existUser);
            User saveUser = userDAO.saveUser(existUser);

            return new ResponseModel(HttpStatus.OK, UserDto.toDto(saveUser), SuccessMsg.USER_UPDATED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMsg.SERVER_ERROR +"\n" + e.getMessage());
        }
    }


    //Block User  --Admin Side
    @Override
    public ResponseModel blockUser(Long userId) {
        try {
            User user = userDAO.userFindById(userId).orElseThrow(() -> new UserNotFoundException(ErrorMsg.USER_NOT_FOUND));

            if (user.getStatus() == Status.ACTIVE) {
                user.setStatus(Status.INACTIVE);
                userDAO.saveUser(user);
                return new ResponseModel(HttpStatus.OK, null, SuccessMsg.USER_BLOCKED);
            }
            user.setStatus(Status.ACTIVE);
            userDAO.saveUser(user);
            return new ResponseModel(HttpStatus.OK, null, SuccessMsg.USER_UNBLOCKED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMsg.SERVER_ERROR +"\n" + e.getMessage());
        }
    }

    //Find User By Id  --User Side
    @Override
    public ResponseModel userFindById(long userId) {

        User existUser = userDAO.findByUserIdAndStatus(userId, Status.ACTIVE).orElseThrow(() -> new UserNotFoundException(ErrorMsg.USER_NOT_FOUND));

        User user = existUser;
        return new ResponseModel(HttpStatus.OK, UserDto.toDto(user), SuccessMsg.USER_GET_SUCCESS);


    }

    //Delete User  --User Side
    @Override
    public ResponseModel deleteUser(Long userId) {
        try {
            User existUser = userDAO.userFindById(userId).orElseThrow(() -> new UserNotFoundException(ErrorMsg.USER_NOT_FOUND));

            userDAO.deleteUser(userId);
            return new ResponseModel(HttpStatus.OK, null, SuccessMsg.USER_DELETED);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, ErrorMsg.SERVER_ERROR +"\n" + e.getMessage());
        }
    }


}
