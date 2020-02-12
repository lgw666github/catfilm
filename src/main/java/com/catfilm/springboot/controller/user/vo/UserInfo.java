package com.catfilm.springboot.controller.user.vo;

import lombok.Data;

@Data
public class UserInfo {
    private Integer uuid;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private int sex;
    private String birthday;
    private String lifeState;
    private String biography;
    private String address;
    private String headAddress;
    private Long beginTime;
    private Long updateTime;


}
