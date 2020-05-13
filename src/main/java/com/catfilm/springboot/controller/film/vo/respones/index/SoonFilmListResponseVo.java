package com.catfilm.springboot.controller.film.vo.respones.index;

import lombok.Data;

@Data
public class SoonFilmListResponseVo {
    private String filmId;
    private Integer filmType;
    private String imgAddress;
    private String filmName;
    private Integer expectNum;
    private String showTime;
}
