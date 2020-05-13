package com.catfilm.springboot.controller.film;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.catfilm.springboot.common.properties.FilmProperties;
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
import com.catfilm.springboot.controller.user.vo.BaseResponseVO;
import com.catfilm.springboot.dao.mapper.FilmInfoTMapper;
import com.catfilm.springboot.service.exception.CommException;
import com.catfilm.springboot.service.film.FilmService;
import javafx.beans.binding.ObjectExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/film/")
public class FilmController {

    @Autowired
    private FilmService service;
    @Autowired
    private FilmProperties filmProperties;

    @RequestMapping(value = "getIndex",method = RequestMethod.GET)
    public BaseResponseVO getIndex() throws CommException {
        Map<String, Object> map=new HashMap<>();
        //banners
        List<BannerResponseVo> bannerInfo = service.getBannerInfo();
        map.put("banners",bannerInfo);

        //hotFilms
        int hotFilmNum=service.getFilmNum("1");
        List<HotFilmListResponseVo> hotFilms = service.getHotFilms();
        Map<String,Object> hotFilmMap=new HashMap<>();
        hotFilmMap.put("filmNum",hotFilmNum);
        hotFilmMap.put("filmInfo",hotFilms);
        map.put("hotFilms",hotFilmMap);

        //soonFilms
        int soonFilmNum = service.getFilmNum("2");
        List<SoonFilmListResponseVo> soonFilms = service.getSoonFilms();
        Map<String,Object> soonFilmMap=new HashMap<>();
        soonFilmMap.put("filmNum",soonFilmNum);
        soonFilmMap.put("filmInfo",soonFilms);
        map.put("soonFilms",soonFilmMap);

        //boxRanking
        List<RankResponseVo> boxRankInfo = service.getBoxRankInfo();
        map.put("boxRanking",boxRankInfo);

        //expectRanking
        List<RankResponseVo> expectRankInfo = service.getExpectRankInfo();
        map.put("expectRanking",expectRankInfo);

        //top10
        List<RankResponseVo> topRankInfo = service.getTopRankInfo();
        map.put("top100",topRankInfo);
        return BaseResponseVO.success(filmProperties.getImgPre(),map);
    }
    @RequestMapping(value = "getConditionList",method = RequestMethod.GET)
    public BaseResponseVO getConditionList(
            @RequestParam(name="catId",required =false ,defaultValue ="99")String catId,
            @RequestParam(name="sourceId",required = false,defaultValue = "99")String sourceId,
            @RequestParam(name="yearId",required = false,defaultValue = "99")String yearId
    ) throws CommException {
         catId=service.checkCondition(catId,"cat");
        sourceId=service.checkCondition(sourceId,"source");
        yearId=service.checkCondition(yearId,"year");

        List<CatInfoResVo> catCondition = service.getCatCondition(catId);
        List<SourceInfoResVo> sourceCondition = service.getSourceCondition(sourceId);
        List<YearInfoResVo> yearCondition = service.getYearCondition(yearId);

        Map<String,Object> map=new HashMap<>();
        map.put("catInfo",catCondition);
        map.put("sourceInfo",sourceCondition);
        map.put("yearInfo",yearCondition);

        return BaseResponseVO.success(map);
    }
    @RequestMapping(value = "getFilms",method = RequestMethod.GET)
    public BaseResponseVO getFilms(QueryFilmParams params) throws CommException {
        IPage<FilmInfoRespVo> page = service.getFilmsByCondition(params);

        return BaseResponseVO.success(page.getCurrent(),page.getPages(),
                filmProperties.getImgPre(),page.getRecords());
    }

    @RequestMapping(value = "films/{searchStr}",method = RequestMethod.GET)
    public BaseResponseVO searchFilmsByType(@PathVariable("searchStr")
                                                        String searchStr, String searchType) throws CommException {

        FilmInfoVo filmInfoVo = service.queryFilmDetail(searchStr, searchType);
        //biography
        String biography = service.getFilmDesc(filmInfoVo.getFilmId());

        //director
        FilmActorRespVo director = service.getFilmDirect(filmInfoVo.getFilmId());

        //actors
        List<FilmActorRespVo> filmActors = service.getFilmActors(filmInfoVo.getFilmId());

        Map<String,Object> actorsMap=new HashMap<>();
        actorsMap.put("director",director);
        actorsMap.put("actors",filmActors);

        //imgs
        ImageRespVo filmImage = service.getFilmImage(filmInfoVo.getFilmId());

        filmInfoVo.getInfo04().put("biography",biography);
        filmInfoVo.getInfo04().put("actors",actorsMap);
        filmInfoVo.setImgs(filmImage);
        return BaseResponseVO.success(filmProperties.getImgPre(),filmInfoVo);

    }
}
