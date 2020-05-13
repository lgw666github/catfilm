package com.catfilm.springboot.controller.cinema.vo;

/*
* 展示影院列表的vo
* */

import lombok.Data;

@Data
public class CinemaListVO {
    private String UUId;
    private String cinemaName;
    private String address;
    private String minimumPrice;
}
