package com.catfilm.springboot.controller.film.vo.respones.condition;

import lombok.Data;
/*
* 影片的类型，喜剧等等
* */

@Data
public class CatInfoResVo {
    private String catId;
    private String catName;
    private String isActive="false";
}
