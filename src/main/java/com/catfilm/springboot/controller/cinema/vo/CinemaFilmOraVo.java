package com.catfilm.springboot.controller.cinema.vo;

import lombok.Data;

import java.util.List;
/*
*  影厅里电影的信息
* */

@Data
public class CinemaFilmOraVo {
    private String filmId;
    private String filmName;
    private String filmLength;
    private String filmType;
    private String filmCats;
    private String actors;
    private String imgAddress;
}
