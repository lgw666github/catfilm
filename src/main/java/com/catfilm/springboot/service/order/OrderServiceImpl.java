package com.catfilm.springboot.service.order;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayResponse;
import com.alipay.api.domain.InsDataAutodamageEstimateResultDetailModel;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catfilm.springboot.common.properties.OrderFileProperties;
import com.catfilm.springboot.common.tools.CalculateUtil;
import com.catfilm.springboot.controller.cinema.vo.CinemaFilmOraVO;
import com.catfilm.springboot.controller.cinema.vo.CinemaHallVO;
import com.catfilm.springboot.controller.order.vo.response.OrderDetailVO;
import com.catfilm.springboot.controller.order.vo.response.PayInfoResVO;
import com.catfilm.springboot.controller.order.vo.response.PayResultVO;
import com.catfilm.springboot.dao.entity.FilmOrderT;
import com.catfilm.springboot.dao.mapper.FilmOrderTMapper;
import com.catfilm.springboot.service.cinema.CinemaService;
import com.catfilm.springboot.service.exception.CommException;
import com.catfilm.springboot.service.order.BO.FilmPriceBO;
import com.catfilm.springboot.zhifubao.trade.config.Configs;
import com.catfilm.springboot.zhifubao.trade.model.ExtendParams;
import com.catfilm.springboot.zhifubao.trade.model.GoodsDetail;
import com.catfilm.springboot.zhifubao.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.catfilm.springboot.zhifubao.trade.model.result.AlipayF2FPrecreateResult;
import com.catfilm.springboot.zhifubao.trade.service.AlipayTradeService;
import com.catfilm.springboot.zhifubao.trade.service.impl.AlipayTradeServiceImpl;
import com.catfilm.springboot.zhifubao.trade.utils.ZxingUtils;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderFileProperties orderFileProperties;

    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private FilmOrderTMapper filmOrderTMapper;

    // 支付宝当面付2.0服务
    private static AlipayTradeService tradeService;

    static {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

//        // 支付宝当面付2.0服务（集成了交易保障接口逻辑）
//        tradeWithHBService = new AlipayTradeWithHBServiceImpl.ClientBuilder().build();
//
//        /** 如果需要在程序中覆盖Configs提供的默认参数, 可以使用ClientBuilder类的setXXX方法修改默认参数 否则使用代码中的默认设置 */
//        monitorService = new AlipayMonitorServiceImpl.ClientBuilder()
//                .setGatewayUrl("http://mcloudmonitor.com/gateway.do").setCharset("GBK")
//                .setFormat("json").build();
    }

    @Override
    public void checkSeats(String seatsId, String fieldId) throws CommException, IOException {
        CinemaHallVO cinemaHallVO = cinemaService.descHallInfo(Integer.valueOf(fieldId));
        File seatFile=new File(orderFileProperties.getFilePre()+cinemaHallVO.getSeatFile());

        @Cleanup  //省去try...catch... 和关流
        BufferedReader br=new BufferedReader(new FileReader(seatFile));

        StringBuilder sb=new StringBuilder();
        String line;
        while((line=br.readLine()) != null){
            sb.append(line);
        }

        JSONObject jsonStr=JSONObject.parseObject(sb.toString());
        String ids = jsonStr.getString("ids");
        List<String> idList = Arrays.asList(ids.split(","));
        List<String> seatList=Arrays.asList(seatsId.split(","));

        if (!idList.containsAll(seatList)){
            throw new CommException(500,"检查不到这个座位"+seatsId);
        }
    }

    @Override
    public void checkSoldSeats(String seatsId, String fieldId) throws CommException {
        CinemaHallVO cinemaHallVO = cinemaService.descHallInfo(Integer.valueOf(fieldId));
        String soldSeats = cinemaHallVO.getSoldSeats();
        List<String> soldList = Arrays.asList(soldSeats.split(","));
        List<String> seatList=Arrays.asList(seatsId.split(","));
        for (String s : seatList) {
            if (soldList.contains(s)){
                throw new CommException(500,s+" 该座位已经售出！。。。");
            }
        }
    }

    @Override
    public OrderDetailVO saveOrderInfo(String userId, String fieldId, String seatsId, String seatName) throws CommException {

        CinemaFilmOraVO cinemaFilmOraVO = cinemaService.descFilmInfo(Integer.valueOf(fieldId));
        FilmPriceBO filmPriceInfo = filmOrderTMapper.getFilmPriceByFieldId(fieldId);

        //订单的总价格=单价*数量
        double orderPrice = CalculateUtil.getOrderPrice(Double.valueOf(filmPriceInfo.getFilmPrice()), seatsId.split(",").length);


        FilmOrderT filmOrderT=new FilmOrderT();

        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        filmOrderT.setCinemaId(Integer.valueOf(filmPriceInfo.getCinemaId()));
        filmOrderT.setFieldId(Integer.valueOf(fieldId));
        filmOrderT.setFilmId(Integer.valueOf(cinemaFilmOraVO.getFilmId()));
        filmOrderT.setFilmPrice(Double.valueOf(filmPriceInfo.getFilmPrice()));
        filmOrderT.setOrderPrice(orderPrice);
        filmOrderT.setOrderUser(Integer.valueOf(userId));
        filmOrderT.setSeatsIds(seatsId);
        filmOrderT.setUuid(uuid);
        filmOrderT.setSeatsName(seatName);

        //插入表中
        int insert = filmOrderTMapper.insert(filmOrderT);
        if(insert>0){
            return filmOrderTMapper.getOrderDetailByOrderId(uuid);
        }
        throw new CommException(500,"用户"+userId+"下单失败");
    }

    @Override
    public IPage<OrderDetailVO> queryOrderByUserId(int curPage,int pageSize,String userId) throws CommException {

        Page<FilmOrderT> page=new Page<>(curPage,pageSize);
        IPage<OrderDetailVO> orderDetailPage = filmOrderTMapper.getOrderDetailByUserId(page, userId);
        return orderDetailPage;
    }

    // 测试当面付2.0生成支付二维码
    @Override
    public String getCode(String orderId,String orderPrice,String filmNum,String cinemaName) throws
            CommException{

        String filePath="";
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        // 需保证商户系统端不能重复，建议通过数据库sequence生成，
        String outTradeNo = orderId;

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = "CatFilm电影院扫码消费";

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = orderPrice;

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body = "购买电影票"+filmNum+"张,共"+orderPrice+"元";

        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = cinemaName;

        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId("2088100200300400500");

        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx小面包", 1000, 1);
        // 创建好一个商品后添加至商品明细列表
        goodsDetailList.add(goods1);

        // 继续创建并添加第一条商品信息，用户购买的产品为“黑人牙刷”，单价为5.00元，购买了两件
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
        goodsDetailList.add(goods2);

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId)
                .setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(orderFileProperties.getAlipayCallBack()) //支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);

                // 需要修改为运行机器上的路径
                 filePath = String.format("erweima/qr-%s.png",
                        response.getOutTradeNo());
                log.info("filePath:" + filePath);

                //生成二维图片
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256,
                        orderFileProperties.getFilePre()+filePath);
                return filePath;

            case FAILED:
                log.error("支付宝预下单失败!!!");
                throw new CommException(500,orderId+"下单失败");
            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                throw new CommException(500,orderId+"下单失败");

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                throw new CommException(500,orderId+"下单失败");
        }
    }

    @Override
    public PayInfoResVO getPayInfoByOrderId(String orderId) throws CommException {
        AbstractWrapper wrapper=new QueryWrapper();
        wrapper.eq("UUID",orderId);
        FilmOrderT filmOrderT = filmOrderTMapper.selectOne(wrapper);
        OrderDetailVO orderDetail = filmOrderTMapper.getOrderDetailByOrderId(orderId);

        //判断filmOrderT、orderDetail是否为空
        if(null==filmOrderT||null==filmOrderT.getUuid()||null==orderDetail){
            throw new CommException(404,"找不到该订单"+orderId);
        }

        int seatNum = filmOrderT.getSeatsIds().split(",").length;

        //这里的orderId为查表里的orderId，不要用传入的ID
        String codeFile = getCode(filmOrderT.getUuid(), orderDetail.getOrderPrice(), String.valueOf(seatNum), orderDetail.getCinemaName());
        PayInfoResVO vo=new PayInfoResVO();
        vo.setOrderId(filmOrderT.getUuid());
        vo.setQRCodeAddress(codeFile);
        return vo;
    }

    @Override
    public PayResultVO getPayResult(String orderId) throws CommException {

        AbstractWrapper wrapper=new QueryWrapper();
        wrapper.eq("UUID",orderId);
        FilmOrderT filmOrderT = filmOrderTMapper.selectOne(wrapper);

        if(null==filmOrderT||null==filmOrderT.getUuid()){
            throw new CommException(404,"找不到该订单"+orderId);
        }

        PayResultVO vo=new PayResultVO();
        vo.setOrderId(filmOrderT.getUuid());
        vo.setOrderStatus(filmOrderT.getOrderStatus());

        return vo;
    }

    @Override
    public void paySuccess(String orderId) throws CommException {
        //幂等性--当订单的状态为待支付时才修改，其他都不修改状态
        AbstractWrapper wrapper=new QueryWrapper();
        wrapper.eq("UUID",orderId);
        wrapper.eq("order_status",0);

        FilmOrderT filmOrderT=new FilmOrderT();
        filmOrderT.setOrderStatus(1);
        int update = filmOrderTMapper.update(filmOrderT, wrapper);
        if(update==0){
            throw new CommException(500,"订单:"+orderId+" 状态修改失败");
        }
    }

    // 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                        response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }
}
