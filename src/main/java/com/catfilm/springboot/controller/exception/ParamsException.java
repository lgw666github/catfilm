package com.catfilm.springboot.controller.exception;

import lombok.Data;

@Data
public class ParamsException extends Exception {
    private int errCode;
    private String msg;

    public ParamsException(int errCode,String msg){
        super(msg);
        this.errCode=errCode;
        this.msg=msg;
    }

}
