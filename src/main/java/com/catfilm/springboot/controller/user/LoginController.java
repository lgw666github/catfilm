package com.catfilm.springboot.controller.user;

import com.catfilm.springboot.controller.user.vo.BaseResponseVO;
import com.catfilm.springboot.common.tools.JwtTokenUtil;
import com.catfilm.springboot.controller.exception.ParamsException;
import com.catfilm.springboot.controller.user.vo.CheckUserVo;
import com.catfilm.springboot.controller.user.vo.TokenInfo;
import com.catfilm.springboot.service.exception.CommException;
import com.catfilm.springboot.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/user/")
public class LoginController {

    @Autowired
    public UserService userService;

    @Autowired
    public JwtTokenUtil jwtTokenUtil;

    @RequestMapping(value = "login",method = RequestMethod.POST)
    public BaseResponseVO login(@RequestBody CheckUserVo checkUserVo) throws ParamsException, CommException {
        //checkUserVo.checkInput();
        boolean isSucc = userService.checkAuth(checkUserVo);
        if (isSucc){
            //返回一个json web token
            String randomkey=jwtTokenUtil.getRandomKey();
            String token=jwtTokenUtil.generateToken(checkUserVo.getUserName(),randomkey);

            TokenInfo tokenInfo=TokenInfo.builder().randomKey(randomkey).token(token).build();
            return BaseResponseVO.success(tokenInfo);
        }else{
            return BaseResponseVO.serviceFailed(400,"用户名或密码错误，登录失败");
        }
    }

    public BaseResponseVO loginOut() throws ParamsException,CommException{

        /**
         * 退出登录
         * 1.从redis中把当前缓存的userinfo给删除了
         */

        return BaseResponseVO.success();

    }

}
