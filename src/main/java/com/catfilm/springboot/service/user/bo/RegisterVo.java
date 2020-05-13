package com.catfilm.springboot.service.user.bo;

import lombok.Data;

@Data
public class RegisterVo {
    private String username;
    private String password;
    private String email;
    private String phone;
    private String address;
}
