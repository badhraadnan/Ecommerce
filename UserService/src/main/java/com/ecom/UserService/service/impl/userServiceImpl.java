package com.ecom.UserService.service.impl;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.LoginDto;
import com.ecom.CommonEntity.dto.UserDto;
import com.ecom.CommonEntity.entity.User;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.UserService.service.UserService;
import com.ecom.UserService.utils.JWTUtil;
import com.ecom.commonRepository.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class userServiceImpl implements UserService {

    @Autowired
    private UserDao userDAO;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JWTUtil jwtUtil;

    //Signup User  --User Side
    @Override
    public ResponseModel SignupUser(UserDto userDto) {
        try {
            Optional<User> existUser = userDAO.findByEmailORMobile(userDto.getEmail(), userDto.getMobile());



            if (existUser.isEmpty()) {
                User user = UserDto.toEntity(userDto);
                user.setPassword(encoder.encode(user.getPassword()));
                User savedUser = userDAO.saveUser(user);

                return new ResponseModel(
                        HttpStatus.OK,
                        UserDto.toDto(savedUser),
                        "User added successfully"
                );
            } else {
                return new ResponseModel(
                        HttpStatus.BAD_REQUEST,
                        null,
                        "User with this email or mobile already exists"
                );
            }
        } catch (Exception e) {
            return new ResponseModel(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    null,
                    "An error occurred while saving user "
            );
        }
    }



    //Get All Users  --Admin User
    @Override
    public ResponseModel getAllUsers() {
        List<User> user = userDAO.userFindAllByStatus(Status.ACTIVE);

        if (user != null) {
            List<UserDto> dto = user.stream()
                    .map(UserDto::toDto)
                    .toList();

            return new ResponseModel(HttpStatus.OK, dto, "Success");
        }
        return null;
    }


    //Update User  --User Side
    @Override
    public ResponseModel updateUser(UserDto userDto) {
        try {
            Optional<User> existUser = userDAO.userFindByIdAndStatus(userDto.getUserId(), Status.ACTIVE);
            //Optional<User> User =userDAO.findByEmailORMobile(userDto.getEmail(), userDto.getMobile());
            if (existUser.isPresent()) {
              //  User user = existUser.get();

                UserDto.updateUser(userDto,existUser.get());
                User saveUser = userDAO.saveUser(existUser.get());

                return new ResponseModel(HttpStatus.OK, UserDto.toDto(saveUser), "User Updated Successfully");
            }else {
            return new ResponseModel(HttpStatus.NOT_FOUND, null, "User Not Exist");
            }
            } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "User Not Updated due to Some Error");
        }
    }


    //Block User  --Admin Side
    @Override
    public ResponseModel blockUser() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> existUser = userDAO.findByEmail(email);

            if (existUser.isPresent()) {
                User user = existUser.get();

                if (user.getStatus() == Status.ACTIVE) {
                    user.setStatus(Status.INACTIVE);
                } else {
                    user.setStatus(Status.ACTIVE);
                }
                userDAO.saveUser(user);
                return new ResponseModel(HttpStatus.OK, null, "User Block Successfully");
            }
            return new ResponseModel(HttpStatus.NOT_FOUND, null, "User Not Exist");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "User Not Block Due to Some Error");
        }
    }

    //Find User By Id  --User Side
    @Override
    public ResponseModel userFindById() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            Optional<User> existUser = userDAO.findByEmailAndStatus(email, Status.ACTIVE);
            if (existUser.isPresent()) {
                User user = existUser.get();

                return new ResponseModel(HttpStatus.OK, UserDto.toDto(user), "User Found");
            }
            return new ResponseModel(HttpStatus.NOT_FOUND, null, "User Not Found");
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR, null, "User Not Founded Due to Some Error");
        }
    }

    //Delete User  --User Side
    @Override
    public ResponseModel deleteUser() {
       try {
           String email = SecurityContextHolder.getContext().getAuthentication().getName();
           Optional<User> existUser = userDAO.findByEmail(email);
           if (existUser.isPresent()) {
               userDAO.deleteUser(email);
               return new ResponseModel(HttpStatus.OK, null, "User Deleted Successfully");
           } else {
               return new ResponseModel(HttpStatus.NOT_FOUND, null, "User Not Exist");
           }
       } catch (Exception e) {
           e.printStackTrace();
           return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR,null,"User Not Deleted Due to some Error");
       }
    }

    //Login User  --User Side
    @Override
    public ResponseModel UserLogin(LoginDto loginDto) {
//        Optional<User> existUser=userDAO.UserLoginByEmailAndPassword(email, password, Status.ACTIVE);
        Authentication authentication=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),loginDto.getPassword()));
        if (authentication != null){

            return new ResponseModel(HttpStatus.OK, jwtUtil.generateKey(loginDto.getEmail()),"User Login Successfully");
        }
        else {
            return new ResponseModel(HttpStatus.NOT_FOUND,null,"User Credential Not Found");
        }
    }

}
