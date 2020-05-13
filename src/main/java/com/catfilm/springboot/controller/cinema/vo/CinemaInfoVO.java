package com.catfilm.springboot.controller.cinema.vo;

/*
* 展示影院详细信息的vo
* */

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CinemaInfoVO {
    private String cinemaId;
    private String imgUri;
    private String cinemaName;
    private String cinemaAddress;
    private String cinemaPhone;
}
