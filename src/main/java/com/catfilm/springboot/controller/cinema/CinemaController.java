package com.catfilm.springboot.controller.cinema;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catfilm.springboot.common.properties.FilmProperties;
import com.catfilm.springboot.controller.cinema.condition.AreaResVO;
import com.catfilm.springboot.controller.cinema.condition.BrandResVO;
import com.catfilm.springboot.controller.cinema.condition.HallTypeResVO;
import com.catfilm.springboot.controller.cinema.vo.*;
import com.catfilm.springboot.controller.user.vo.BaseResponseVO;
import com.catfilm.springboot.service.cinema.CinemaService;
import com.catfilm.springboot.service.cinema.bo.QueryCinemaParams;
import com.catfilm.springboot.service.cinema.bo.QueryConditionParams;
import com.catfilm.springboot.service.cinema.bo.QueryFieldParams;
import com.catfilm.springboot.service.exception.CommException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cinema/")
public class CinemaController {

    @Autowired
    private FilmProperties filmProperties;

    @Autowired
    private CinemaService service;

    @RequestMapping(value = "getFields",method = RequestMethod.GET)
    public BaseResponseVO getFields(String cinemaId) throws CommException {
        CinemaInfoVO cinemaInfoVO = service.queryCinemaInfoById(Integer.valueOf(cinemaId));
        List<CinemaFilmInfoVO> cinemaFilmInfoList = service.descFilmList(Integer.valueOf(cinemaId));

        List<Map<String,CinemaFilmInfoVO>> mapList=new ArrayList<>();
        cinemaFilmInfoList.stream().forEach((film)->{
            Map<String,CinemaFilmInfoVO> filmMap=new HashMap<>();
            filmMap.put("filmInfo", film);
            mapList.add(filmMap);
        });

        Map<String,Object> map=new HashMap<String,Object>();
        map.put("cinemaInfo",cinemaInfoVO);
        map.put("filmList",mapList);
        return BaseResponseVO.success(filmProperties.getImgPre(),map);
    }

    @RequestMapping(value = "getFieldInfo",method = RequestMethod.POST)
    public BaseResponseVO getFieldInfo(@RequestBody QueryFieldParams params)throws CommException{
        CinemaInfoVO cinemaInfoVO = service.queryCinemaInfoById(Integer.valueOf(params.getCinemaId()));
        CinemaHallVO cinemaHallVO = service.descHallInfo(Integer.valueOf(params.getFieldId()));
        CinemaFilmOraVO cinemaFilmOraVO = service.descFilmInfo(Integer.valueOf(params.getFieldId()));

        Map<String,Object> map=new HashMap<>();
        map.put("hallInfo",cinemaHallVO);
        map.put("filmInfo",cinemaFilmOraVO);
        map.put("cinemaInfo",cinemaInfoVO);

        return BaseResponseVO.success(filmProperties.getImgPre(),map);
    }

    @RequestMapping(value = "getCinemas",method = RequestMethod.GET)
    public BaseResponseVO getCinemas(QueryCinemaParams params) throws CommException{
        Page<CinemaListVO> cinemaListVOPage = service.queryCinemaInfo(params);
        Map<String,Object> map=new HashMap<>();
        map.put("cinemas",cinemaListVOPage.getRecords());
        return BaseResponseVO.success(cinemaListVOPage.getCurrent(),cinemaListVOPage.getPages(),
                filmProperties.getImgPre(),map);
    }

    @RequestMapping(value = "getCondition",method = RequestMethod.GET)
    public BaseResponseVO getCondition (
            @RequestParam(name = "brandId",required = false,defaultValue = "99")Integer brandId,
            @RequestParam(name = "hallType",required = false,defaultValue = "99")Integer hallType,
            @RequestParam(name = "areaId",required = false,defaultValue = "99")Integer areaId) throws CommException
    {

        areaId= service.checkConditionParam(areaId,"area")?areaId:99;
        brandId= service.checkConditionParam(brandId,"brand")?brandId:99;
        hallType= service.checkConditionParam(hallType,"hall")?hallType:99;

        List<AreaResVO> areaResVOS = service.descAreaList(areaId);
        List<BrandResVO> brandResVOS = service.descBrandList(brandId);
        List<HallTypeResVO> hallTypeResVOS = service.descHallTypeList(hallType);

        Map<String,List> map=new HashMap<>();
        map.put("areaList",areaResVOS);
        map.put("halltypeList",hallTypeResVOS);
        map.put("brandList",brandResVOS);

        return BaseResponseVO.success(map);
    }
}
