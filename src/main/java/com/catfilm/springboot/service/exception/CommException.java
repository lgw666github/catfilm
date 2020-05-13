package com.catfilm.springboot.service.exception;

import lombok.Data;

@Data
public class CommException extends Exception {
    private int errCode;
    private String msg;

    public CommException(int errCode,String msg){
       // super(msg);
        this.errCode=errCode;
        this.msg=msg;
    }

}
