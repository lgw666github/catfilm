package com.catfilm.springboot.controller.film.vo.respones.condition;

import lombok.Data;
/*
* 影片上映的时间
* */
@Data
public class YearInfoResVo {
    private String yearId;
    private String yearName;
    private String isActive="false";
}
