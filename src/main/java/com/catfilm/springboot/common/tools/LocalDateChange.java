package com.catfilm.springboot.common.tools;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateChange {
    public static String localDateTime2Str(LocalDateTime time){
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        return formatter.format(time);
    }
}
