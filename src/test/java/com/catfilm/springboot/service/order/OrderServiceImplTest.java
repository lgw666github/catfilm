package com.catfilm.springboot.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.catfilm.springboot.controller.order.vo.response.OrderDetailVO;
import com.catfilm.springboot.controller.order.vo.response.PayInfoResVO;
import com.catfilm.springboot.controller.order.vo.response.PayResultVO;
import com.catfilm.springboot.service.exception.CommException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrderServiceImplTest {

    @Autowired
    private OrderService service;

    @Test
    void checkSeats() throws IOException, CommException {
        service.checkSeats("1,3,7,0","1");
    }

    @Test
    void checkSoldSeats() throws IOException, CommException {
        service.checkSoldSeats("9","1");
    }

    @Test
    void saveOrderInfo() throws IOException, CommException {
        OrderDetailVO orderDetailVO = service.saveOrderInfo("1", "1", "7,8,9", "aaaaaa");
        System.out.println(orderDetailVO);
    }

    @Test
    void queryOrderByUser() throws CommException {
        IPage<OrderDetailVO> orderPage = service.queryOrderByUserId(1, 10, "1");
        System.out.println(orderPage.getRecords());

    }

    @Test
    void getPayInfoByOrderId() throws CommException {
        PayInfoResVO payInfoByOrderId = service.getPayInfoByOrderId("f5325484eb4f4478b09272b9fd881fac");
        System.out.println(payInfoByOrderId);
    }

    @Test
    void getPayResult() throws CommException {
        PayResultVO PayResult = service.getPayResult("f5325484eb4f4478b09272b9fd881fac");
        System.out.println(PayResult);
    }
}