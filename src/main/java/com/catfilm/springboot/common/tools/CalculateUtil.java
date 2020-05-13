package com.catfilm.springboot.common.tools;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculateUtil {
    public static double getOrderPrice(double filmPrice,int filmNum){
        BigDecimal price=new BigDecimal(filmPrice);
        BigDecimal num=new BigDecimal(filmNum);

        BigDecimal totalPrice = price.multiply(num);
        //小数点后留两位，四舍五入
        return totalPrice.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
