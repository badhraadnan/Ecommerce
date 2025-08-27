package com.ecom.CommonEntity.dto;

import com.ecom.CommonEntity.Enum.Role;
import com.ecom.CommonEntity.Enum.Status;
import com.ecom.CommonEntity.entity.Address;
import com.ecom.CommonEntity.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String mobile;

    private String gender;

    private Role role;

    private Status status;

    public static User toEntity(UserDto userDto){
        return User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .password(userDto.getPassword())
                .mobile(userDto.getMobile())
                .gender(userDto.getGender())
                .role(userDto.getRole())
                .status(Status.ACTIVE)
                .build();
    }

    public static UserDto toDto(User user){
        return UserDto.builder()
                .userId(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .password(user.getPassword())
                .mobile(user.getMobile())
                .gender(user.getGender())
                .role(user.getRole())
                .status(user.getStatus())
                .build();
    }

    public static void updateUser(UserDto userDto, User user) {
        if (userDto.getFirstName() != null) {
            user.setFirstName(userDto.getFirstName());
        }
        if (userDto.getLastName() != null) {
            user.setLastName(userDto.getLastName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
        if (userDto.getPassword() != null) {
            user.setPassword(userDto.getPassword());
        }
        if (userDto.getMobile() != null) {
            user.setMobile(userDto.getMobile());
        }
        if (userDto.getGender() != null) {
            user.setGender(userDto.getGender());
        }
//        if (userDto.getAddress() != null) {
//            user.setAddress(userDto.getAddress());
//        }
//        if (userDto.getCity() != null) {
//            user.setCity(userDto.getCity());
//        }
//        if (userDto.getState() != null) {
//            user.setState(userDto.getState());
//        }
//        if (userDto.getCountry() != null) {
//            user.setCountry(userDto.getCountry());
//        }
//        if (userDto.getZipCode() != null) {
//            user.setZipCode(userDto.getZipCode());
//        }

        user.setUpdatedAt(LocalDateTime.now());
    }

}
