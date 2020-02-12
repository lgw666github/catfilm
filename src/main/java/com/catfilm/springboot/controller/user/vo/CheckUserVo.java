package com.catfilm.springboot.controller.user.vo;

import com.catfilm.springboot.controller.check.CheckParams;
import com.catfilm.springboot.controller.exception.ParamsException;
import lombok.Data;

@Data
public class CheckUserVo implements CheckParams {
    private String userName;
    private String passwd;

    public CheckUserVo(String userName, String passwd) {
        this.userName = userName;
        this.passwd = passwd;
    }

    @Override
    public void checkInput() throws ParamsException {
        if(this.passwd.length()<6)
            throw new ParamsException(500,"密码长度大于6");
    }
}
