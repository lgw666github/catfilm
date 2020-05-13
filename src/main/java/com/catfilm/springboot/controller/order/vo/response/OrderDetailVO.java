package com.catfilm.springboot.controller.order.vo.response;

import lombok.Data;

@Data
public class OrderDetailVO {
    private String orderId;
    private String filmName;
    private String fieldTime;
    private String cinemaName;
    private String seatsName;
    private String orderPrice;
    private String orderStatus;

}
