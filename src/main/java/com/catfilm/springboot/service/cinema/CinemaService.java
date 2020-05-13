package com.catfilm.springboot.service.cinema;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catfilm.springboot.controller.cinema.condition.AreaResVO;
import com.catfilm.springboot.controller.cinema.condition.BrandResVO;
import com.catfilm.springboot.controller.cinema.condition.HallTypeResVO;
import com.catfilm.springboot.controller.cinema.vo.*;
import com.catfilm.springboot.service.cinema.bo.QueryCinemaParams;
import com.catfilm.springboot.service.exception.CommException;

import java.util.List;

public interface CinemaService {

    //根据查询条件获取影院信息
    Page<CinemaListVO> queryCinemaInfo(QueryCinemaParams queryCinemaParams) throws CommException;

    //检查查询条件的入参
    boolean checkConditionParam(int id,String conditionType) throws CommException;

    // 查询影院的条件
    List<AreaResVO> descAreaList(int areaId) throws CommException;
    List<BrandResVO> descBrandList(int brandId) throws CommException;
    List<HallTypeResVO> descHallTypeList(int hallTypeId) throws CommException;

    // 根据影院id获取影院信息
    CinemaInfoVO queryCinemaInfoById(int cinemaId) throws CommException;

    //根据影院id获取影院场次信息
    List<CinemaFilmInfoVO> descFilmList(int cinemaId) throws CommException;

    //根据场次编号获取电影信息
    CinemaFilmOraVO descFilmInfo(int fieldId) throws CommException;

    //根据场次编号获取放映厅信息
    CinemaHallVO descHallInfo(int fieldId) throws CommException;
}
