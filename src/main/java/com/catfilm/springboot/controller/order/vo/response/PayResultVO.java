package com.catfilm.springboot.controller.order.vo.response;

import lombok.Data;

@Data
public class PayResultVO {
    private String orderId;
    private Integer orderStatus;
    private String orderMsg="订单支付消息";
}
