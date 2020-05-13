package com.catfilm.springboot.service.cinema;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catfilm.springboot.controller.cinema.condition.HallTypeResVO;
import com.catfilm.springboot.controller.cinema.vo.CinemaFilmInfoVO;
import com.catfilm.springboot.controller.cinema.vo.CinemaHallVO;
import com.catfilm.springboot.controller.cinema.vo.CinemaListVO;
import com.catfilm.springboot.service.cinema.bo.QueryCinemaParams;
import com.catfilm.springboot.service.exception.CommException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class CinemaServiceImplTest {

    @Autowired
    private CinemaService service;
    
    @Test
    void queryCinemaInfo() throws CommException {
        QueryCinemaParams params=new QueryCinemaParams();
        params.setBrandId("1");
        params.setDistrictId("2");
        params.setHallType("2");
        Page<CinemaListVO> page = service.queryCinemaInfo(params);
        log.info("currPage:{},totalPage:{},currSize:{},records:{}",
                page.getCurrent(),page.getTotal(),page.getSize(),page.getRecords());
    }
    

    @Test
    void descAreaList() {
    }

    @Test
    void descBrandList() {
    }

    @Test
    void descHallTypeList() {
    }

    @Test
    void queryCinemaInfoById() {
    }

    @Test
    void descFileList() throws CommException {
        List<CinemaFilmInfoVO> cinemaFilmInfoVOS = service.descFilmList(1);
        System.out.println(cinemaFilmInfoVOS);

    }

    @Test
    void descFilmInfo() {
    }

    @Test
    void descHallInfo() throws CommException {
        CinemaHallVO cinemaHallVO = service.descHallInfo(1);
        System.out.println(cinemaHallVO);
    }
}