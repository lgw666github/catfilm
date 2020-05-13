package com.catfilm.springboot.dao.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catfilm.springboot.controller.order.vo.response.OrderDetailVO;
import com.catfilm.springboot.dao.entity.FilmOrderT;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.catfilm.springboot.service.order.BO.FilmPriceBO;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author lgw
 * @since 2020-02-21
 */
public interface FilmOrderTMapper extends BaseMapper<FilmOrderT> {

   OrderDetailVO getOrderDetailByOrderId(String orderId);

   IPage<OrderDetailVO> getOrderDetailByUserId(Page<FilmOrderT> page, String userId);

   FilmPriceBO getFilmPriceByFieldId(String fieldId);

   String showSoldSeats(String fieldId);

}
