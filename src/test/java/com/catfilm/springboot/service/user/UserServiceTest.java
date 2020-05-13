package com.catfilm.springboot.service.user;

import com.catfilm.springboot.controller.user.vo.UserInfo;
import com.catfilm.springboot.dao.mapper.NextUserTMapper;
import com.catfilm.springboot.service.exception.CommException;
import com.catfilm.springboot.service.user.bo.RegisterVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void queryById() throws CommException {
        UserInfo userInfo = userService.queryById(2);
        System.out.println(userInfo);
    }

    @Test
    public void registerUser() throws CommException {
        RegisterVo registerVo=new RegisterVo();
        registerVo.setUsername("tom");
        registerVo.setPassword("123456");
        boolean b = userService.registerUser(registerVo);
        System.out.println(b);
    }

    @Test
    public void checkUserName() throws CommException {
        boolean checkhasUserName = userService.checkhasUserName("tom");
        System.out.println("checkhasUserName ->"+checkhasUserName);
    }

    @Test
    public void updateUserInfo() throws CommException{
        UserInfo userInfo=new UserInfo();
        userInfo.setUuid(4);
        userInfo.setNickname("little tom");
        userInfo.setAddress("hongkong");
        //userInfo.setLifeState("2");
        UserInfo userInfo1 = userService.updateUserInfo(userInfo);
        System.out.println(userInfo1);
    }
}