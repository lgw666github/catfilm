package com.catfilm.springboot.controller.film.vo.respones.filmDetail;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class FilmInfoVo {
    private String filmId;
    private String filmName;
    private String filmEnName;
    private String imgAddress;
    private String score;
    private String scoreNum;
    private String totalBox;
    private String info01;
    private String info02;
    private String info03;
    private Map<String,Object> info04=new HashMap<>();
    private ImageRespVo imgs;
}
