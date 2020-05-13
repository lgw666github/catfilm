package com.catfilm.springboot.controller.order;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catfilm.springboot.common.properties.FilmProperties;
import com.catfilm.springboot.controller.order.vo.response.OrderDetailVO;
import com.catfilm.springboot.controller.order.vo.response.PayInfoResVO;
import com.catfilm.springboot.controller.order.vo.response.PayResultVO;
import com.catfilm.springboot.controller.user.vo.BaseResponseVO;
import com.catfilm.springboot.service.exception.CommException;
import com.catfilm.springboot.service.order.OrderService;
import com.catfilm.springboot.service.user.UserService;
import com.catfilm.springboot.zhifubao.trade.config.Configs;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private FilmProperties filmProperties;

    static{
        Configs.init("zfbinfo.properties");
    }

    @RequestMapping(value = "buyTickets",method = RequestMethod.GET)
    public BaseResponseVO buyTickets(String fieldId,String soldSeats,String seatsName) throws CommException {
        //检查soldSeats是否是正常票号
        try {
            orderService.checkSeats(soldSeats,fieldId);
        } catch (IOException e) {
            throw new CommException(404,"座位信息未找到");
        }

        //检查soldSeats是否没售出
        orderService.checkSoldSeats(soldSeats,fieldId);

        String userId=userService.getUserId();

        OrderDetailVO orderDetailVO = orderService.saveOrderInfo(userId, fieldId, soldSeats, seatsName);

        return BaseResponseVO.success(orderDetailVO);
    }

    @RequestMapping(value = "getOrderInfo",method = RequestMethod.GET)
    public BaseResponseVO getOrderInfo(String nowPage,String pageSize) throws CommException {
        IPage page=new Page(Long.valueOf(nowPage),Long.valueOf(pageSize));

        IPage<OrderDetailVO> orderDetailPage = orderService.queryOrderByUserId(Integer.valueOf(nowPage), Integer.valueOf(pageSize),
                userService.getUserId());

        return BaseResponseVO.success(Long.valueOf(nowPage),orderDetailPage.getTotal(),filmProperties.getImgPre(),orderDetailPage.getRecords());
    }

    @RequestMapping(value = "getPayInfo" ,method = RequestMethod.GET)
    public BaseResponseVO getPayInfo(String orderId) throws CommException {
        PayInfoResVO payInfo = orderService.getPayInfoByOrderId(orderId);

        return BaseResponseVO.success(filmProperties.getImgPre(),payInfo);
    }

    @RequestMapping(value = "getPayResult" ,method = RequestMethod.GET)
    public BaseResponseVO getPayResult(String orderId,
                                       @RequestParam(name="tryNum", required = false,defaultValue = "1")Integer tryNum)
                                        throws CommException {
        if(tryNum<4){
            PayResultVO payResult = orderService.getPayResult(orderId);
            return BaseResponseVO.success(payResult);
        }
            throw new CommException(500,"订单"+orderId+"支付失败");
    }

    @RequestMapping(value = "alipay/callback",method = RequestMethod.POST)
    public void alipayCallBack(HttpServletRequest request, HttpServletResponse response) throws AlipayApiException {
        //1.将request中的params转成map，移除sign_type,sign(代码自动移除)
        Map<String,String> paramMap=new HashMap();

        //Map<String, String[]> parameterMap = request.getParameterMap();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()){
            String paramName=parameterNames.nextElement();
            paramMap.put(paramName,request.getParameter(paramName));
            //System.out.println(paramName+" -> "+request.getParameter(paramName));
        }
        paramMap.remove("sign_type");

        //2.支付宝校验数据是否正常，返回boolean,这个方法只支持AlipayApiException异常
        boolean isSuccess = AlipaySignature.rsaCheckV2(paramMap, Configs.getAlipayPublicKey(),
                                "utf-8", Configs.getSignType());

        //3.根据返回结果，输出信息
        PrintWriter writer=null;
        try {
             writer = response.getWriter();
            if(isSuccess){
                orderService.paySuccess(paramMap.get("out_trade_no"));
                writer.print("success");
            }else{
                writer.print("No success");
            }
        } catch (IOException | CommException e) {
            writer.print("NoSuccess");
            log.error("支付失败："+e.getMessage());
        }finally {
            if (null==writer){
                writer.close();
            }
        }
    }
}
