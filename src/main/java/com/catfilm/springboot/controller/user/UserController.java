package com.catfilm.springboot.controller.user;

import com.catfilm.springboot.controller.user.vo.BaseResponseVO;
import com.catfilm.springboot.controller.user.vo.UserInfo;
import com.catfilm.springboot.service.exception.CommException;
import com.catfilm.springboot.service.user.UserService;
import com.catfilm.springboot.service.user.bo.RegisterVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNullApi;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/user/")
@Api("用户模块的相关Api")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户名注册", notes = "用户名注册提示")
    @ApiImplicitParam(name = "registerVo",
            value = "用户的注册信息",
            required = true, dataType = "RegisterVo")
    @RequestMapping(value = "regist",method = RequestMethod.POST)
    public BaseResponseVO registUser(@RequestBody RegisterVo registerVo) throws CommException {
        boolean isSucc = userService.registerUser(registerVo);
        if (isSucc)
            return BaseResponseVO.success("用户注册成功！");
        return BaseResponseVO.serviceFailed("用户名注册失败。。。");
    }

    @ApiOperation(value = "检查用户名是否重复", notes = "检查用户名是否重复的提示")
    @ApiImplicitParam(name = "userName",
            value = "待检查的用户名",
            paramType = "query", required = true, dataType = "String")
    @RequestMapping(value = "checkUserName",method = RequestMethod.GET)
    public BaseResponseVO checkUserName(String userName) throws CommException {
        boolean checkhasUserName = userService.checkhasUserName(userName);
        if (!checkhasUserName)
            return BaseResponseVO.success("用户名不存在。。。");

        return BaseResponseVO.serviceFailed("用户名已存在。。。");
    }

    @RequestMapping(value = "queryById",method = RequestMethod.GET)
    public BaseResponseVO queryById(Integer id) throws CommException {
        UserInfo userInfo = userService.queryById(id);
        return BaseResponseVO.success(userInfo);
    }

    @RequestMapping(value = "queryUserInfo",method = RequestMethod.GET)
    public UserInfo queryUserInfo() throws CommException{
        UserInfo userInfo = userService.queryByUserName();
        return userInfo;
    }

    @RequestMapping(value = "updateUserInfo",method = RequestMethod.POST)
    public BaseResponseVO updateUserInfo(UserInfo userInfo) throws CommException {

        UserInfo userInfo1 = userService.updateUserInfo(userInfo);
        return BaseResponseVO.success(userInfo1);
    }

}
