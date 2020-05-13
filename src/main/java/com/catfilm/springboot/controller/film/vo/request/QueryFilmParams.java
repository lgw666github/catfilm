package com.catfilm.springboot.controller.film.vo.request;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.catfilm.springboot.controller.check.CheckParams;
import com.catfilm.springboot.controller.exception.ParamsException;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class QueryFilmParams implements CheckParams {

    private String showType="1";   //热映的、即将上映、经典
    private String sortId="1";      //排序的方式 热门、时间、评价
    private String catId="99";
    private String sourceId="99";
    private String yearId="99";
    private String nowPage="1";
    private String pageSize="18";
    public static Map<String,String> sortMap=new HashMap();

    static{
        sortMap.put("1","film_box_office");
        sortMap.put("2","film_date");
        sortMap.put("3","film_score");
    }

    @Override
    public void checkInput() throws ParamsException {
        //showType字段 影片状态,1-正在热映，2-即将上映，3-经典影片
        //校验sortId字段 排序的方式 热门、时间、评价

    }



    public AbstractWrapper getWrapper(){

        AbstractWrapper wrapper=new QueryWrapper();
        wrapper.eq("film_status",this.getShowType());
        if(!"99".equals(this.getCatId())){
            wrapper.like("film_cats","#"+ this.getCatId() +"#");
        }
        if(!"99".equals(this.getSourceId())){
            wrapper.eq("film_source",this.getSourceId());
        }
        if (!"99".equals(this.getYearId())){
            wrapper.eq("film_date",this.getYearId());
        }



        return  wrapper;
    }
}
