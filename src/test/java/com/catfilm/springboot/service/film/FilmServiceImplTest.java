package com.catfilm.springboot.service.film;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.catfilm.springboot.controller.film.vo.request.QueryFilmParams;
import com.catfilm.springboot.controller.film.vo.respones.filmDetail.FilmActorRespVo;
import com.catfilm.springboot.controller.film.vo.respones.filmList.FilmInfoRespVo;
import com.catfilm.springboot.controller.film.vo.respones.index.BannerResponseVo;
import com.catfilm.springboot.controller.film.vo.respones.index.HotFilmListResponseVo;
import com.catfilm.springboot.controller.film.vo.respones.index.RankResponseVo;
import com.catfilm.springboot.controller.film.vo.respones.index.SoonFilmListResponseVo;
import com.catfilm.springboot.service.exception.CommException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class FilmServiceImplTest {

    @Autowired
    private FilmService service;

    @Test
    void getBannerInfo() throws CommException {
        List<BannerResponseVo> bannerInfo = service.getBannerInfo();
        System.out.println(bannerInfo);
    }

    @Test
    void getHotFilms() throws CommException  {
        List<HotFilmListResponseVo> hotFilms = service.getHotFilms();
        System.out.println(hotFilms);
    }

    @Test
    void getSoonFilms()  throws CommException {
        List<SoonFilmListResponseVo> soonFilms = service.getSoonFilms();
        System.out.println(soonFilms);
    }

    @Test
    void getBoxRankInfo()  throws CommException {
        List<RankResponseVo> boxRankInfo = service.getBoxRankInfo();
        System.out.println(boxRankInfo);
    }

    @Test
    void getExpectRankInfo() throws CommException  {
        List<RankResponseVo> expectRankInfo = service.getExpectRankInfo();
        System.out.println(expectRankInfo);
    }

    @Test
    void getTopRankInfo()  throws CommException {
        List<RankResponseVo> topRankInfo = service.getTopRankInfo();
        System.out.println(topRankInfo);
    }

    @Test
    void checkCondition()  throws CommException {
    }

    @Test
    void getCatCondition()  throws CommException {
    }

    @Test
    void getSourceCondition() throws CommException  {
    }

    @Test
    void getYearCondition()  throws CommException {
    }

    @Test
    void getFilmsByCondition() throws CommException{
        QueryFilmParams params=new QueryFilmParams();
        params.setShowType("2");
        params.setSortId("1");
        params.setYearId("13");
        params.setSourceId("1");
        params.setCatId("2");
        IPage<FilmInfoRespVo> filmsByCondition = service.getFilmsByCondition(params);
        System.out.println(filmsByCondition.getRecords());
    }

    @Test
    void getDirectInfoByFilmId() throws CommException{
        FilmActorRespVo filmDirect = service.getFilmDirect("2");
        System.out.println(filmDirect);

        List<FilmActorRespVo> filmActors = service.getFilmActors("2");
        System.out.println(filmActors);
    }
}