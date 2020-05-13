package com.catfilm.springboot.service.film;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.catfilm.springboot.controller.film.vo.request.QueryFilmParams;
import com.catfilm.springboot.controller.film.vo.respones.condition.CatInfoResVo;
import com.catfilm.springboot.controller.film.vo.respones.condition.SourceInfoResVo;
import com.catfilm.springboot.controller.film.vo.respones.condition.YearInfoResVo;
import com.catfilm.springboot.controller.film.vo.respones.filmDetail.FilmActorRespVo;
import com.catfilm.springboot.controller.film.vo.respones.filmDetail.FilmInfoVo;
import com.catfilm.springboot.controller.film.vo.respones.filmDetail.ImageRespVo;
import com.catfilm.springboot.controller.film.vo.respones.filmList.FilmInfoRespVo;
import com.catfilm.springboot.controller.film.vo.respones.index.BannerResponseVo;
import com.catfilm.springboot.controller.film.vo.respones.index.HotFilmListResponseVo;
import com.catfilm.springboot.controller.film.vo.respones.index.RankResponseVo;
import com.catfilm.springboot.controller.film.vo.respones.index.SoonFilmListResponseVo;
import com.catfilm.springboot.service.exception.CommException;

import java.util.List;

public interface FilmService {

    //获取首页film，getindex
    //获取Banner信息
    List<BannerResponseVo> getBannerInfo() throws CommException;

    //获取热映影片
    List<HotFilmListResponseVo> getHotFilms() throws CommException;

    //获取即将上映的影片
    List<SoonFilmListResponseVo> getSoonFilms() throws CommException;

    //获取电影数
    int getFilmNum(String filmType);

    //获取票房排行信息
    List<RankResponseVo> getBoxRankInfo() throws CommException;

    //获取人气排行信息
    List<RankResponseVo> getExpectRankInfo() throws CommException;

    //获取Top100信息
    List<RankResponseVo> getTopRankInfo() throws CommException;

    //条件查询
    //检查条件的有效性
    String checkCondition(String conditionId,String type) throws CommException;

    //三种条件查询
    //影片的类型
    List<CatInfoResVo> getCatCondition(String catId) throws CommException;
    //影片的国家
    List<SourceInfoResVo> getSourceCondition(String sourceId) throws CommException;
    //影片上映的时间
    List<YearInfoResVo> getYearCondition(String yearId) throws CommException;

    //根据条件查询电影列表
    IPage<FilmInfoRespVo> getFilmsByCondition(QueryFilmParams params) throws CommException;

    //根据编号（0），名称（1）查找用户
    FilmInfoVo queryFilmDetail(String searchStr, String type);

    //获取电影的描述信息
    String getFilmDesc(String filmId) throws CommException;

    //获取电影的图片信息
    ImageRespVo getFilmImage(String filmId) throws CommException;

    //获取电影的导演信息
    FilmActorRespVo getFilmDirect(String filmId) throws CommException;

    //获取电影的演员信息
    List<FilmActorRespVo> getFilmActors(String filmId) throws CommException;
}
