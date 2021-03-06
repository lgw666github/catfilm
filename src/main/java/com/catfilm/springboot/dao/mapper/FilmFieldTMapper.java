package com.catfilm.springboot.dao.mapper;

import com.catfilm.springboot.controller.cinema.vo.CinemaFilmInfoVO;
import com.catfilm.springboot.controller.cinema.vo.CinemaFilmOraVO;
import com.catfilm.springboot.controller.cinema.vo.CinemaHallVO;
import com.catfilm.springboot.dao.entity.FilmFieldT;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 放映场次表 Mapper 接口
 * </p>
 *
 * @author lgw
 * @since 2020-02-12
 */
public interface FilmFieldTMapper extends BaseMapper<FilmFieldT> {

    public List<CinemaFilmInfoVO> descFieldInfo(String cinemaId);

    public CinemaFilmOraVO descFilmInfo(String fieldId);

    public CinemaHallVO descHallInfo(String fieldId);
}
