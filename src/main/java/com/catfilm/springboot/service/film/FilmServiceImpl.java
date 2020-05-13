package com.catfilm.springboot.service.film;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catfilm.springboot.common.tools.LocalDateChange;
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
import com.catfilm.springboot.dao.entity.*;
import com.catfilm.springboot.dao.mapper.*;
import com.catfilm.springboot.service.exception.CommException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {

    @Autowired
    private FilmActorTMapper filmActorTMapper;
    @Autowired
    private FilmActorRelaTMapper filmActorRelaTMapper;

    @Autowired
    private FilmCatDictTMapper filmCatDictTMapper;
    @Autowired
    private FilmSourceDictTMapper filmSourceDictTMapper;
    @Autowired
    private FilmYearDictTMapper filmYearDictTMapper;

    @Autowired
    private FilmBannerTMapper filmBannerTMapper;

    @Autowired
    private FilmDetailTMapper filmDetailTMapper;
    @Autowired
    private FilmInfoTMapper filmInfoTMapper;

    @Override
    public List<BannerResponseVo> getBannerInfo() throws CommException {

        AbstractWrapper wrapper=new QueryWrapper();
        wrapper.eq("is_valid ",0);
        List<FilmBannerT> list = filmBannerTMapper.selectList(wrapper);
        List<BannerResponseVo> result=new ArrayList<>();

        list.stream().forEach((info)->{
            BannerResponseVo vo=new BannerResponseVo();
            vo.setBannerId(info.getUuid().toString());
            vo.setBannerAddress(info.getBannerAddress());
            vo.setBannerUrl(info.getBannerUrl());
            result.add(vo);
        });

        return result;
    }

    @Override
    public List<HotFilmListResponseVo> getHotFilms() throws CommException {
        //热映信息，一页记录8条，只看第一页
        AbstractWrapper wrapper=new QueryWrapper();
        wrapper.eq("film_status",1);
        IPage<FilmInfoT> page=new Page(1,8);

        IPage<FilmInfoT> iPage = filmInfoTMapper.selectPage(page, wrapper);
        List<HotFilmListResponseVo> collect = iPage.getRecords().stream().map((info) -> {
            HotFilmListResponseVo vo = new HotFilmListResponseVo();
            vo.setFilmId(info.getUuid().toString());
            vo.setFilmName(info.getFilmName());
            vo.setFilmScore(info.getFilmScore());
            vo.setFilmType(info.getFilmType());
            vo.setImgAddress(info.getImgAddress());
            return vo;
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<SoonFilmListResponseVo> getSoonFilms() throws CommException {
        //热映信息，一页记录8条，只看第一页
        AbstractWrapper wrapper=new QueryWrapper();
        wrapper.eq("film_status",2);
        IPage<FilmInfoT> page=new Page(1,8);

        IPage<FilmInfoT> iPage = filmInfoTMapper.selectPage(page, wrapper);
        List<SoonFilmListResponseVo> collect = iPage.getRecords().stream().map((info) -> {
            SoonFilmListResponseVo vo = new SoonFilmListResponseVo();
            vo.setFilmId(info.getUuid().toString());
            vo.setFilmName(info.getFilmName());
            vo.setExpectNum(info.getFilmPresalenum());
            vo.setShowTime(LocalDateChange.localDateTime2Str(info.getFilmTime()));
            vo.setFilmType(info.getFilmType());
            vo.setImgAddress(info.getImgAddress());
            return vo;
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public int getFilmNum(String filmType) {
        //1->热映 2-》 即将上映
        AbstractWrapper wrapper=new QueryWrapper();
        if("1".equals(filmType)){
            wrapper.eq("film_status",1);
        }else{
            wrapper.eq("film_status",2);
        }
        return filmInfoTMapper.selectCount(wrapper);
    }

    @Override
    public List<RankResponseVo> getBoxRankInfo() throws CommException {
        //票房排行，肯定在热映的电影中的，页面展示1页，10个 降序排列
        Page<FilmInfoT> page=new Page(1,10);
        page.setDesc("film_box_office"); //票房来排名
        AbstractWrapper wrapper=new QueryWrapper();
        wrapper.eq("film_status",1);

        IPage<FilmInfoT> iPage = filmInfoTMapper.selectPage(page, wrapper);
        List<RankResponseVo> collect = iPage.getRecords().stream().map((info) -> {
            RankResponseVo vo = new RankResponseVo();
            vo.setBoxNum(info.getFilmBoxOffice());
            vo.setFilmId(info.getUuid().toString());
            vo.setFilmName(info.getFilmName());
            vo.setImgAddress(info.getImgAddress());
            return vo;
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<RankResponseVo> getExpectRankInfo() throws CommException {
        //即将上映的电影，按预售票来排序，展示一页10行
        Page<FilmInfoT> page=new Page<>(1,10);
        page.setDesc("film_preSaleNum");
        AbstractWrapper wrapper=new QueryWrapper();
        wrapper.eq("film_status",2);
        IPage<FilmInfoT> iPage = filmInfoTMapper.selectPage(page, wrapper);
        List<RankResponseVo> collect = iPage.getRecords().stream().map((info) -> {
            RankResponseVo vo = new RankResponseVo();
            vo.setFilmId(info.getUuid().toString());
            vo.setFilmName(info.getFilmName());
            vo.setImgAddress(info.getImgAddress());
            vo.setExpectNum(info.getFilmPresalenum());
            return vo;
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<RankResponseVo> getTopRankInfo() throws CommException {
        //得分排名前10名肯定也是在热映上的，按得分排序，
        Page page=new Page(1,10);
        page.setDesc("film_score");

        AbstractWrapper wrapper=new QueryWrapper();
        wrapper.eq("film_status",1);

        IPage<FilmInfoT> iPage = filmInfoTMapper.selectPage(page, wrapper);
        List<RankResponseVo> collect = iPage.getRecords().stream().map((info) -> {
            RankResponseVo vo = new RankResponseVo();
            vo.setFilmId(info.getUuid().toString());
            vo.setFilmName(info.getFilmName());
            vo.setImgAddress(info.getImgAddress());
            vo.setScore(info.getFilmScore());
            return vo;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public String checkCondition(String conditionId, String type) throws CommException {
        //cat,source,year
        if (conditionId.equals("99"))
            return "99";

       switch (type) {
           case "cat":
               FilmCatDictT filmCatDictT = filmCatDictTMapper.selectById(conditionId);
               if(filmCatDictT==null){
                  return "99";
               }
               break;
           case "source":
               FilmSourceDictT filmSourceDictT = filmSourceDictTMapper.selectById(conditionId);
               if(filmSourceDictT==null){
                   return "99";
               }
               break;
           case "year":
               FilmYearDictT filmYearDictT = filmYearDictTMapper.selectById(conditionId);
               if(filmYearDictT==null){
                   return "99";
               }
       }
        return conditionId;
    }

    @Override
    public List<CatInfoResVo> getCatCondition(String catId) throws CommException {

        List<FilmCatDictT> filmCatDictTS = filmCatDictTMapper.selectList(null);
        List<CatInfoResVo> result = filmCatDictTS.stream().map((info) -> {
            CatInfoResVo vo = new CatInfoResVo();
            vo.setCatId(info.getUuid().toString());
            vo.setCatName(info.getShowName());
            if (vo.getCatId().equals(catId))
                vo.setIsActive("true");
            return vo;
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<SourceInfoResVo> getSourceCondition(String sourceId) throws CommException {

        List<FilmSourceDictT> filmSourceDictTS = filmSourceDictTMapper.selectList(null);
        List<SourceInfoResVo> collect = filmSourceDictTS.stream().map((info) -> {
            SourceInfoResVo vo = new SourceInfoResVo();
            vo.setSourceId(info.getUuid().toString());
            vo.setSourceName(info.getShowName());
            if (vo.getSourceId().equals(sourceId))
                vo.setIsActive("true");
            return vo;
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public List<YearInfoResVo> getYearCondition(String yearId) throws CommException {
        List<FilmYearDictT> filmYearDictTS = filmYearDictTMapper.selectList(null);
        List<YearInfoResVo> collect = filmYearDictTS.stream().map((info) -> {
            YearInfoResVo vo = new YearInfoResVo();
            vo.setYearId(info.getUuid().toString());
            vo.setYearName(info.getShowName());
            if (vo.getYearId().equals(yearId))
                vo.setIsActive("true");
            return vo;
        }).collect(Collectors.toList());

        return collect;
    }

    @Override
    public IPage<FilmInfoRespVo> getFilmsByCondition(QueryFilmParams params) throws CommException {
       Page page=new Page(Long.valueOf(params.getNowPage()),Long.valueOf(params.getPageSize()));
       page.setDesc(QueryFilmParams.sortMap.get(params.getSortId()));

        String catId = checkCondition(params.getCatId(), "cat");
        String sourceId = checkCondition(params.getSourceId(), "source");
        String yearId = checkCondition(params.getYearId(), "year");

        params.setCatId(catId);
        params.setSourceId(sourceId);
        params.setYearId(yearId);

        AbstractWrapper wrapper=params.getWrapper();

        IPage<FilmInfoT> iPage = filmInfoTMapper.selectPage(page, wrapper);
        List<FilmInfoRespVo> collect = iPage.getRecords().stream().map((info) -> {
            FilmInfoRespVo vo = new FilmInfoRespVo();
            vo.setFilmId(info.getUuid().toString());
            vo.setFilmName(info.getFilmName());
            vo.setFilmScore(info.getFilmScore());
            vo.setFilmType(info.getFilmType());
            vo.setImgAddress(info.getImgAddress());
            return vo;
        }).collect(Collectors.toList());

        IPage<FilmInfoRespVo> result=new Page(Long.valueOf(params.getNowPage()),Long.valueOf(params.getPageSize()));
        result.setRecords(collect);
        return result;
    }

    @Override
    public FilmInfoVo queryFilmDetail(String searchStr, String type){
        FilmInfoVo vo=null;
        //0--id,1--name
        if("0".equals(type)){
            vo=filmInfoTMapper.queryFilmById(searchStr);
            return vo;
        }
            return filmInfoTMapper.queryFilmByName(searchStr);
    }
    
    @Override
    public String getFilmDesc(String filmId) throws CommException {
        AbstractWrapper wrapper=new QueryWrapper();
        wrapper.eq("film_id",filmId);
        List<FilmDetailT> list = filmDetailTMapper.selectList(wrapper);
        if(null!=list&&list.size()>0){
            FilmDetailT detailT=list.get(0);
            return detailT.getBiography();
        }
        throw new CommException(404,"no desc ...");
    }

    @Override
    public ImageRespVo getFilmImage(String filmId) throws CommException {
        AbstractWrapper wrapper=new QueryWrapper();
        wrapper.eq("film_id",filmId);
        List<FilmDetailT> list = filmDetailTMapper.selectList(wrapper);
        if(null!=list&&list.size()>0){
            FilmDetailT detailT=list.get(0);
            String[] split = detailT.getFilmImgs().split(",");
            ImageRespVo vo =new ImageRespVo();
            if(null!=split&&split.length==5){
                vo.setMainImg(split[0]);
                vo.setImg01(split[1]);
                vo.setImg02(split[2]);
                vo.setImg03(split[3]);
                vo.setImg04(split[4]);
            }
            return vo;
        }
        throw new CommException(404,"no desc ...");
    }

    @Override
    public FilmActorRespVo getFilmDirect(String filmId) throws CommException {

        return filmDetailTMapper.getDirectInfoByFilmId(filmId);
    }

    @Override
    public List<FilmActorRespVo> getFilmActors(String filmId) throws CommException {
        return filmActorTMapper.queryFilmActorById(filmId);
    }
}
