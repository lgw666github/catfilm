package com.catfilm.springboot.controller.user;

public class UserNameFromToken {

    public static ThreadLocal<String> threadLocal=new ThreadLocal();

    public static String getUserName(){
        return threadLocal.get();
    }
}
