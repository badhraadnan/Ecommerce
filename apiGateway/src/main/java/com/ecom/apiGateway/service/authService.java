package com.ecom.apiGateway.service;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.LoginDto;
import com.ecom.CommonEntity.dto.UserDto;
import com.ecom.CommonEntity.entity.User;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.apiGateway.utils.JWTUtil;
import com.ecom.commonRepository.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class authService {

    @Autowired
    private UserDao userDAO;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JWTUtil jwtUtil;

    public ResponseModel SignupUser(UserDto userDto) {
        try {
            Optional<User> existUser = userDAO.findByEmailORMobile(userDto.getEmail(), userDto.getMobile());

            if (existUser.isEmpty()) {
                User user = UserDto.toEntity(userDto);
                user.setPassword(encoder.encode(user.getPassword()));
                User savedUser = userDAO.saveUser(user);

                return new ResponseModel(HttpStatus.OK, UserDto.toDto(savedUser), "User added successfully");
            } else {
                return new ResponseModel(HttpStatus.BAD_REQUEST, null, "User with this email or mobile already exists"
                );
            }
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR,null,"An error occurred while saving user "
            );
        }
    }


    public ResponseModel UserLogin(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        if (authentication != null) {

            User user = userDAO.findByEmail(loginDto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String token = jwtUtil.generateKey(user.getEmail(),user.getRole().toString());

            return new ResponseModel(HttpStatus.OK, token, "User Login Successfully");
        } else {
            return new ResponseModel(HttpStatus.NOT_FOUND, null, "User Credential Not Found");
        }
    }

    public ResponseModel forgotPassword(UserDto userDto) {
        Optional<User> existUser = userDAO.findByUserIdAndStatus(userDto.getUserId(), Status.ACTIVE);
        if (existUser.isEmpty()){
            return new ResponseModel(HttpStatus.BAD_REQUEST,null,"User Not Exist..");
        }
        User user = existUser.get();
        if (encoder.matches(userDto.getPassword(), user.getPassword())) {
            return new ResponseModel(HttpStatus.BAD_REQUEST, null, "New password cannot be same as old password");
        }
        UserDto.updateUser(userDto,user);

        user.setPassword(encoder.encode(userDto.getPassword()));
        User saveUser = userDAO.saveUser(user);

        return new ResponseModel(HttpStatus.OK,saveUser,"success");

    }

}
