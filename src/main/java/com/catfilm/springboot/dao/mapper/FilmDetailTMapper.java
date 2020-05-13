package com.catfilm.springboot.dao.mapper;

import com.catfilm.springboot.controller.film.vo.respones.filmDetail.FilmActorRespVo;
import com.catfilm.springboot.dao.entity.FilmDetailT;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 影片主表 Mapper 接口
 * </p>
 *
 * @author lgw
 * @since 2020-02-17
 */
public interface FilmDetailTMapper extends BaseMapper<FilmDetailT> {

    FilmActorRespVo  getDirectInfoByFilmId(String filmId);
}
