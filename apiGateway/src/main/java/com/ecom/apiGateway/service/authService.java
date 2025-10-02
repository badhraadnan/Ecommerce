package com.ecom.apiGateway.service;

import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.dto.LoginDto;
import com.ecom.CommonEntity.dto.UserDto;
import com.ecom.CommonEntity.entity.User;
import com.ecom.CommonEntity.model.ErrorMsg;
import com.ecom.CommonEntity.model.ResponseModel;
import com.ecom.CommonEntity.model.SuccessMsg;
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

                return new ResponseModel(HttpStatus.OK, UserDto.toDto(savedUser), SuccessMsg.USER_SIGNUP_SUCCESS);

            } else {
                return new ResponseModel(HttpStatus.BAD_REQUEST, null, "User with this email or mobile already exists"
                );
            }
        } catch (Exception e) {
            return new ResponseModel(HttpStatus.INTERNAL_SERVER_ERROR,null,ErrorMsg.SERVER_ERROR + "\n" +e.getMessage()
            );
        }
    }


    public ResponseModel UserLogin(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        if (authentication != null) {

            User user = userDAO.findByEmail(loginDto.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException(ErrorMsg.USER_NOT_FOUND));

            String token = jwtUtil.generateKey(user.getEmail(),user.getRole().toString());

            return new ResponseModel(HttpStatus.OK, token, SuccessMsg.USER_LOGIN_SUCCESS);
        } else {
            return new ResponseModel(HttpStatus.NOT_FOUND, null, ErrorMsg.INVALID_CREDENTIALS);
        }
    }

    public ResponseModel forgotPassword(UserDto userDto) {
        Optional<User> existUser = userDAO.findByUserIdAndStatus(userDto.getUserId(), Status.ACTIVE);
        if (existUser.isEmpty()){
            return new ResponseModel(HttpStatus.BAD_REQUEST,null, ErrorMsg.USER_NOT_FOUND);
        }
        User user = existUser.get();
        if (encoder.matches(userDto.getPassword(), user.getPassword())) {
            return new ResponseModel(HttpStatus.BAD_REQUEST, null, ErrorMsg.PASSWORD_DUPLICATION);
        }
        UserDto.updateUser(userDto,user);

        user.setPassword(encoder.encode(userDto.getPassword()));
        User saveUser = userDAO.saveUser(user);

        return new ResponseModel(HttpStatus.OK,saveUser, SuccessMsg.USER_RESET_PASSWORD_SUCCESS);

    }

}
