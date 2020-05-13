package com.catfilm.springboot.dao.mapper;

import com.catfilm.springboot.controller.film.vo.respones.filmDetail.FilmActorRespVo;
import com.catfilm.springboot.controller.film.vo.respones.filmDetail.FilmInfoVo;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class FilmInfoTMapperTest {

    @Autowired
    private FilmInfoTMapper mapper;

    @Autowired
    private FilmActorTMapper actorTMapper;

    @Test
    void queryFilmById() {
        FilmInfoVo filmInfoRespVo = mapper.queryFilmById("2");
        System.out.println(filmInfoRespVo);
    }

    @Test
    void queryFilmByName() {
        FilmInfoVo filmInfoRespVo = mapper.queryFilmByName("药神");
        System.out.println(filmInfoRespVo);
    }

    @org.junit.Test
    public void descActorInfo(){
        List<FilmActorRespVo> filmActorVos = actorTMapper.queryFilmActorById("2");
        filmActorVos.stream().forEach((info)->{
                    System.out.println(info);
                }
        );
    }
}