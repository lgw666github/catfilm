package com.catfilm.springboot.controller.user;

import com.catfilm.springboot.controller.user.vo.BaseResponseVO;
import com.catfilm.springboot.controller.exception.ParamsException;
import com.catfilm.springboot.service.exception.CommException;
import com.catfilm.springboot.service.exception.TransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

public class ExceptionController {

    @ExceptionHandler(ParamsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponseVO handParamsException(ParamsException e){
        return BaseResponseVO.serviceFailed(e.getErrCode(),e.getMsg());
    }

    @ExceptionHandler(CommException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponseVO handCommonException(CommException e){
        return BaseResponseVO.serviceFailed(e.getErrCode(),e.getMsg());
    }

    @ExceptionHandler(TransactionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public BaseResponseVO handTranscationException(TransactionException e){
        return BaseResponseVO.serviceFailed(e.getErrCode(),e.getMsg());
    }

}
