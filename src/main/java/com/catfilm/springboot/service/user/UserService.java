package com.catfilm.springboot.service.user;

import com.catfilm.springboot.controller.user.vo.CheckUserVo;
import com.catfilm.springboot.controller.user.vo.UserInfo;
import com.catfilm.springboot.service.exception.CommException;
import com.catfilm.springboot.service.user.bo.RegisterVo;

public interface UserService {

    //查询用户信息
    public UserInfo queryById(Integer id) throws CommException;

    //根据用户名查询用户
    public UserInfo queryByUserName() throws CommException;

    //注册用户
    public boolean registerUser(RegisterVo registerVo) throws CommException;

    //检查用户姓名
    public boolean checkhasUserName(String userName) throws CommException;

    //检查用户名和密码
    public boolean checkAuth(CheckUserVo checkUserVo) throws CommException;

    //修改用户信息
    public UserInfo updateUserInfo(UserInfo userInfo) throws CommException;

    //获取用户的ID
    String getUserId() throws CommException;
}
