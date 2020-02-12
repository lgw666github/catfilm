package com.catfilm.springboot.controller.check;

import com.catfilm.springboot.controller.exception.ParamsException;

public interface CheckParams {
    public void checkInput() throws ParamsException;
}
