package com.catfilm.springboot.dao.mapper;

import com.catfilm.springboot.controller.film.vo.respones.filmDetail.FilmActorRespVo;
import com.catfilm.springboot.dao.entity.FilmActorT;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 演员表 Mapper 接口
 * </p>
 *
 * @author lgw
 * @since 2020-02-17
 */
public interface FilmActorTMapper extends BaseMapper<FilmActorT> {
    List<FilmActorRespVo> queryFilmActorById(String filmId);
}
