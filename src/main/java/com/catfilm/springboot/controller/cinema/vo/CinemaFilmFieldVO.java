package com.catfilm.springboot.controller.cinema.vo;

import lombok.Data;
/*
*  影厅里电影的场次信息
* */

@Data
public class CinemaFilmFieldVO {
    private String fieldId;
    private String beginTime;
    private String endTime;
    private String language;
    private String hallName;
    private String price;
}
