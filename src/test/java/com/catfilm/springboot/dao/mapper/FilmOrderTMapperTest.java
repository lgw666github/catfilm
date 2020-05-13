package com.catfilm.springboot.dao.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catfilm.springboot.controller.order.vo.response.OrderDetailVO;
import com.catfilm.springboot.dao.entity.FilmOrderT;
import com.catfilm.springboot.service.order.BO.FilmPriceBO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class FilmOrderTMapperTest {
    @Autowired
    private FilmOrderTMapper mapper;

    @Test
    void getOrderDetailByOrderId() {
        OrderDetailVO orderDetailByOrderId = mapper.getOrderDetailByOrderId("415sdf58ew12ds5fe1");
        System.out.println(orderDetailByOrderId);

    }

    @Test
    void getOrderDetailByUserId() {
        IPage<FilmOrderT> page=new Page(1,10);
        IPage<OrderDetailVO> orderDetailByUserId = mapper.getOrderDetailByUserId((Page<FilmOrderT>) page, "1");
        System.out.println(orderDetailByUserId.getRecords());
    }

    @Test
    void getFilmPriceByFieldId() {
        FilmPriceBO filmPriceByFieldId = mapper.getFilmPriceByFieldId("1");
        System.out.println(filmPriceByFieldId);
    }

    @Test
    void showSoldSeats() {
        String s = mapper.showSoldSeats("1");
        System.out.println(s);
    }
}