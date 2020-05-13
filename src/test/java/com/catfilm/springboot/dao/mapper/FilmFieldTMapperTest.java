package com.catfilm.springboot.dao.mapper;

import com.catfilm.springboot.controller.cinema.vo.CinemaFilmInfoVO;
import com.catfilm.springboot.controller.cinema.vo.CinemaFilmOraVO;
import com.catfilm.springboot.controller.cinema.vo.CinemaHallVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilmFieldTMapperTest {
    @Autowired
    private FilmFieldTMapper mapper;


    @Test
    public void descFieldInfo() {
        List<CinemaFilmInfoVO> filmInfoVos = mapper.descFieldInfo("7");
        System.out.println(filmInfoVos.size());

    }

    @Test
    public void descFilmInfo() {
        CinemaFilmOraVO cinemaFilmOraVo = mapper.descFilmInfo("1");
        System.out.println(cinemaFilmOraVo);
    }

    @Test
    public void descHallInfo() {
        CinemaHallVO cinemaHallVO = mapper.descHallInfo("1");
        System.out.println(cinemaHallVO);
    }


}