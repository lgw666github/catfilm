package com.catfilm.springboot.dao.mapper;

import com.catfilm.springboot.controller.film.vo.respones.filmDetail.FilmInfoVo;
import com.catfilm.springboot.dao.entity.FilmInfoT;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 影片主表 Mapper 接口
 * </p>
 *
 * @author lgw
 * @since 2020-02-17
 */
public interface FilmInfoTMapper extends BaseMapper<FilmInfoT> {

    FilmInfoVo queryFilmById(String filmId);

    FilmInfoVo queryFilmByName(@Param("filmName") String filmName);
}
