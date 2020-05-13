package com.catfilm.springboot.service.cinema.bo;

import lombok.Data;

@Data
public class QueryConditionParams {
    private String brandId;
    private String hallType;
    private String areaId;
}
