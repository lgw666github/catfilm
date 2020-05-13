package com.catfilm.springboot.controller.film.vo.respones.filmList;

import lombok.Data;

@Data
public class FilmInfoRespVo {
    private String filmId;
    private Integer filmType;
    private String imgAddress;
    private String filmName;
    private String filmScore;
}
