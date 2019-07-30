package com.mmall.controller.portal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCodeEnum;
import com.mmall.common.ResponseService;
import com.mmall.pojo.User;
import com.mmall.service.IOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);
    private final IOrderService orderService;

    public OrderController(IOrderService orderService) {
        this.orderService = orderService;
    }


    @RequestMapping("/pay.do")
    @ResponseBody
    public ResponseService pay(HttpSession session, Long orderNo, HttpServletRequest request){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String path = request.getServletContext().getRealPath("upload");
        if (user != null){
            return orderService.pay(user.getId(),orderNo,path);
        }
        return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getDesc());
    }

    @RequestMapping("/alipay_callback.do")
    @ResponseBody
    public Object alipayCallback(HttpServletRequest request){
        Map<String,String> params = Maps.newHashMap();
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> m : parameterMap.entrySet()){
            String key = m.getKey();
            String[] value = m.getValue();
            String values = "";
            for (int i = 0; i < value.length; i++) {
                values = (i == value.length - 1 ? values + value[i] + "," :values + value[i]) ;
            }
            params.put(key,values);
        }
        LOGGER.info("支付宝回调,sign:{},trade_status:{},参数:{}",params.get("sign"),params.get("trade_status"),params.toString());
        //验证回调的正确性,是不是支付宝发的.并且呢还要避免重复通知.
        params.remove("sign_type");
        try {
            boolean alipayRSACheckedV2 = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(),"utf-8",Configs.getSignType());
            if(!alipayRSACheckedV2){
                return ResponseService.creatByError("非法请求,验证不通过");
            }
        } catch (AlipayApiException e) {
            LOGGER.error("支付宝验证回调异常",e);
        }
        //TODO 验证各种数据

        ResponseService serverResponse = orderService.aliCallback(params);
        if(serverResponse.isSuccess()){
            return Const.AlipayCallback.RESPONSE_SUCCESS;
        }
        return Const.AlipayCallback.RESPONSE_FAILED;
    }


    @RequestMapping("/query_order_pay_status.do")
    @ResponseBody
    public ResponseService<Boolean> queryOrderPayStatus(HttpSession session, Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user != null){
            ResponseService responseService = orderService.queryOrderPayStatus(user.getId(),orderNo);
            if (responseService.isSuccess()){
                return ResponseService.creatBySuccess(true);
            }else {
                return ResponseService.creatBySuccess(false);
            }
        }
        return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),ResponseCodeEnum.NEED_LOGIN.getDesc());
    }
}
