package com.catfilm.springboot.dao.mapper;

import com.catfilm.springboot.controller.cinema.vo.CinemaFilmInfoVo;
import com.catfilm.springboot.controller.cinema.vo.CinemaFilmOraVo;
import com.catfilm.springboot.controller.cinema.vo.CinemaHallVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FilmFieldTMapperTest {
    @Autowired
    private FilmFieldTMapper mapper;

    @Test
    public void descFieldInfo() {
        List<CinemaFilmInfoVo> filmInfoVos = mapper.descFieldInfo("1");
        System.out.println(filmInfoVos);

    }

    @Test
    public void descFilmInfo() {
        CinemaFilmOraVo cinemaFilmOraVo = mapper.descFilmInfo("1");
        System.out.println(cinemaFilmOraVo);
    }

    @Test
    public void descHallInfo() {
        CinemaHallVO cinemaHallVO = mapper.descHallInfo("1");
        System.out.println(cinemaHallVO);
    }
}