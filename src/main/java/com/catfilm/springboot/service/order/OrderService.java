package com.catfilm.springboot.service.order;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.catfilm.springboot.controller.order.vo.response.OrderDetailVO;
import com.catfilm.springboot.controller.order.vo.response.PayInfoResVO;
import com.catfilm.springboot.controller.order.vo.response.PayResultVO;
import com.catfilm.springboot.service.exception.CommException;

import java.io.IOException;

public interface OrderService {

    //检查座位是否正确
    void checkSeats(String seatsId,String fieldId) throws CommException, IOException;

    //检查座位是否已卖
    void checkSoldSeats(String seatsId,String fieldId) throws CommException;

    //保存订单信息 先插入订单信息，再查询订单
    OrderDetailVO saveOrderInfo(String userId,String fieldId,String seatsId,String seatName) throws CommException;

    //根据用户id查询订单信息
    IPage<OrderDetailVO> queryOrderByUserId(int curPage,int pageSize,String userId) throws CommException;

    //获取二维码,是根据订单编号生成的二维码，如果付款失败，就应该取消订单（缺这个），重新下一个新的订单
    String getCode(String orderId,String orderPrice,String filmNum,String cinemaName) throws CommException;

    //getPayInfo
    PayInfoResVO getPayInfoByOrderId(String orderId) throws CommException;

    //getPayResult
    PayResultVO getPayResult(String orderId) throws CommException;

    //paySuccess
    void paySuccess(String orderId) throws CommException;
}
