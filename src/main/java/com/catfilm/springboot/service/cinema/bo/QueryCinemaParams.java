package com.catfilm.springboot.service.cinema.bo;

import com.alibaba.druid.sql.visitor.functions.If;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.catfilm.springboot.dao.entity.FilmCinemaT;
import lombok.Data;

@Data
public class QueryCinemaParams{

    //数值99代表的是全部，默认值
    private String brandId="99";
    private String hallType="99";
    private String districtId="99";
    private String pageSize="10";
    private String nowPage="1";

    //根据判断是否是默认值来拼过滤条件
    public QueryWrapper<FilmCinemaT> getWapper(){

        QueryWrapper<FilmCinemaT> wrapper=new QueryWrapper();
        if(this.getBrandId()!=null&&!this.getBrandId().equals("99")){
            wrapper.eq("brand_id",this.getBrandId());
        }
        if(this.getDistrictId()!=null&&!this.getDistrictId().equals("99")){
            wrapper.eq("area_id",this.getDistrictId());
        }
        if(this.getHallType()!=null&&!this.getHallType().equals("99")){
            //like默认会在两边加两个%，所以不需要加
            wrapper.like("hall_ids","#"+this.getHallType()+"#");
        }
        return wrapper;
    }
}
