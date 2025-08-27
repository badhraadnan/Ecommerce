package com.ecom.CommonEntity.dto;

import com.ecom.CommonEntity.Enum.Role;
import lombok.Data;

@Data
public class LoginDto {

    private String email;
    private String password;
}
