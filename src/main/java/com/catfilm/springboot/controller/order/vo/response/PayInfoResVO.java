package com.catfilm.springboot.controller.order.vo.response;

import lombok.Data;

@Data
public class PayInfoResVO {
    private String orderId;
    private String QRCodeAddress;
}
