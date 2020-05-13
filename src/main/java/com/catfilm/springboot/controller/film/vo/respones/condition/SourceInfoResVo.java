package com.catfilm.springboot.controller.film.vo.respones.condition;

import lombok.Data;

/*
*  影片的国家
* */

@Data
public class SourceInfoResVo {
    private String sourceId;
    private String sourceName;
    private String isActive="false";
}
