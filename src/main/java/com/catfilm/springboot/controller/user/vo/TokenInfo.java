package com.catfilm.springboot.controller.user.vo;

import com.catfilm.springboot.controller.check.CheckParams;
import com.catfilm.springboot.controller.exception.ParamsException;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenInfo implements CheckParams {
    private String randomKey;
    private String token;

    @Override
    public void checkInput() throws ParamsException {
        //自定义校验
    }
}
