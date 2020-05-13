package com.catfilm.springboot.service.cinema;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catfilm.springboot.controller.cinema.condition.AreaResVO;
import com.catfilm.springboot.controller.cinema.condition.BrandResVO;
import com.catfilm.springboot.controller.cinema.condition.HallTypeResVO;
import com.catfilm.springboot.controller.cinema.vo.*;
import com.catfilm.springboot.dao.entity.FilmAreaDictT;
import com.catfilm.springboot.dao.entity.FilmBrandDictT;
import com.catfilm.springboot.dao.entity.FilmCinemaT;
import com.catfilm.springboot.dao.entity.FilmHallDictT;
import com.catfilm.springboot.dao.mapper.*;
import com.catfilm.springboot.service.cinema.bo.QueryCinemaParams;
import com.catfilm.springboot.service.exception.CommException;
import com.google.common.collect.Lists;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CinemaServiceImpl implements CinemaService {
    @Autowired
    private FilmAreaDictTMapper filmAreaDictTMapper;
    @Autowired
    private FilmBrandDictTMapper filmBrandDictTMapper;
    @Autowired
    private FilmCinemaTMapper filmCinemaTMapper;
    @Autowired
    private FilmFieldTMapper filmFieldTMapper;
    @Autowired
    private FilmHallDictTMapper filmHallDictTMapper;
    @Autowired
    private FilmHallFilmInfoTMapper filmHallFilmInfoTMapper;

    @Autowired
    private FilmOrderTMapper filmOrderTMapper;

    @Override
    public Page<CinemaListVO> queryCinemaInfo(QueryCinemaParams queryCinemaParams) throws CommException {
        Page page=new Page(Long.valueOf(queryCinemaParams.getNowPage()),Long.valueOf(queryCinemaParams.getPageSize()));
        QueryWrapper<FilmCinemaT> wapper = queryCinemaParams.getWapper();
        IPage<FilmCinemaT> iPage = filmCinemaTMapper.selectPage(page, wapper);

        Page cinemaPage=new Page<CinemaListVO>(
                Long.valueOf(queryCinemaParams.getNowPage()),Long.valueOf(queryCinemaParams.getPageSize()));

        List<CinemaListVO> collect = iPage.getRecords().stream().map(
                (data) -> {
                    CinemaListVO vo = new CinemaListVO();
                    vo.setUUId(data.getUuid().toString());
                    vo.setCinemaName(data.getCinemaName());
                    vo.setAddress(data.getCinemaAddress());
                    vo.setMinimumPrice(data.getMinimumPrice().toString());

                    return vo;
                }).collect(Collectors.toList());

        cinemaPage.setRecords(collect);
        return cinemaPage;
    }

    @Override
    public boolean checkConditionParam(int id,String conditionType) throws CommException{
        //检查当前id是否在各种场景的id范围内
        switch (conditionType){
            case "area":
                FilmAreaDictT filmAreaDictT = filmAreaDictTMapper.selectById(id);
                if(null!=filmAreaDictT){
                    return true;
                }
                return false;
            case "brand":
                FilmBrandDictT filmBrandDictT = filmBrandDictTMapper.selectById(id);
                if(null!=filmBrandDictT){
                    return true;
                }
                return false;
            case "hall":
                FilmHallDictT filmHallDictT = filmHallDictTMapper.selectById(id);
                if(null!=filmHallDictT){
                    return true;
                }
                return false;
        }
        return false;
    }

    @Override
    public List<AreaResVO> descAreaList(int areaId)  throws CommException{
        //选中这个id，将该id的isActive设为true -- 前端会显示选中的状态
        List<FilmAreaDictT> filmAreaDictTS = filmAreaDictTMapper.selectList(null);
        List<AreaResVO> collect = filmAreaDictTS.stream().map((data) -> {
            AreaResVO vo = new AreaResVO();
            vo.setAreaId(data.getUuid().toString());
            vo.setAreaName(data.getShowName());
            if (data.getUuid() == areaId) {
                vo.setIsActive("true");
            }else{
                vo.setIsActive("false");
            }
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<BrandResVO> descBrandList(int brandId)  throws CommException{
        List<FilmBrandDictT> filmBrandDictTS = filmBrandDictTMapper.selectList(null);
        List<BrandResVO> collect = filmBrandDictTS.stream().map((data) -> {
            BrandResVO vo = new BrandResVO();
            vo.setBrandId(data.getUuid().toString());
            vo.setBrandName(data.getShowName());
            if (brandId == data.getUuid()) {
                vo.setIsActive("true");
            }else{
                vo.setIsActive("false");
            }
            return vo;
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<HallTypeResVO> descHallTypeList(int hallTypeId)  throws CommException{
        List<FilmHallDictT> filmHallDictTS = filmHallDictTMapper.selectList(null);
        List<HallTypeResVO> collect = filmHallDictTS.stream().map((data) -> {
            HallTypeResVO vo = new HallTypeResVO();
            vo.setHallTypeId(data.getUuid().toString());
            vo.setHallTypeName(data.getShowName());
            if (hallTypeId == data.getUuid()) {
                vo.setIsActive("true");
            }else{
                vo.setIsActive("false");
            }
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public CinemaInfoVO queryCinemaInfoById(int cinemaId)  throws CommException{
        FilmCinemaT filmCinemaT = filmCinemaTMapper.selectById(cinemaId);
        CinemaInfoVO vo =CinemaInfoVO.builder()
                        .cinemaAddress(filmCinemaT.getCinemaAddress())
                        .cinemaId(filmCinemaT.getUuid().toString())
                        .cinemaName(filmCinemaT.getCinemaName())
                        .cinemaPhone(filmCinemaT.getCinemaPhone())
                        .imgUri(filmCinemaT.getImgAddress()).build();
        return vo;
    }

    @Override
    public List<CinemaFilmInfoVO> descFilmList(int cinemaId)  throws CommException{
        //检查id是否存在,不存在list里size是0

        AbstractWrapper wrapper=new QueryWrapper();
        wrapper.eq("cinema_id",cinemaId);
        Integer integer = filmFieldTMapper.selectCount(wrapper);

        if(integer==0){
            return new ArrayList<>();
        }

        return  filmFieldTMapper.descFieldInfo(cinemaId + "");
    }

    @Override
    public CinemaFilmOraVO descFilmInfo(int fieldId) throws CommException {
        //检查id是否存在，不存在返回null
        AbstractWrapper wrapper=new QueryWrapper();
        wrapper.eq("UUID",fieldId);
        Integer integer = filmFieldTMapper.selectCount(wrapper);
        if(integer==0){
            return new CinemaFilmOraVO();
        }
        return filmFieldTMapper.descFilmInfo(String.valueOf(fieldId));
    }

    @Override
    public CinemaHallVO descHallInfo(int fieldId) throws CommException {
        //检查id是否存在，不存在返回null
        AbstractWrapper wrapper=new QueryWrapper();
        wrapper.eq("UUID",fieldId);
        Integer integer = filmFieldTMapper.selectCount(wrapper);
        if(integer==0){
           throw new CommException(404,"没有这个场次"+fieldId);
        }
        CinemaHallVO cinemaHallVO = filmFieldTMapper.descHallInfo(String.valueOf(fieldId));
        String soldSeats = filmOrderTMapper.showSoldSeats(String.valueOf(fieldId));
        cinemaHallVO.setSoldSeats(soldSeats);
        return cinemaHallVO;
    }
}
