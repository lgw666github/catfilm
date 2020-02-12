package com.catfilm.springboot.controller.user;

public class UserNameFromToken {

    public static ThreadLocal<String> threadlocal=new ThreadLocal();

    public static String getUserName(){
        return threadlocal.get();
    }
}
