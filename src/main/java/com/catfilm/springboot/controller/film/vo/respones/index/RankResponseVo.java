package com.catfilm.springboot.controller.film.vo.respones.index;

import lombok.Data;

@Data
public class RankResponseVo {
    private String filmId;
    private String imgAddress;
    private String filmName;

    private Integer boxNum;  //票房排行
    private String score;    //评分排行
    private Integer expectNum;  //期望排行 人气排行

}
