package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseService;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import com.mmall.vo.CartVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/shipping")
public class ShippingController {
    private final IShippingService shippingService;

    public ShippingController(IShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @RequestMapping("add.do")
    @ResponseBody
    public ResponseService<Map<String,Integer>> add(HttpSession session, Shipping shipping){
        User current_user = (User) session.getAttribute(Const.CURRENT_USER);
        if (current_user != null){
            return shippingService.add(current_user.getId(),shipping);
        }
        return ResponseService.creatByError("请登录");
    }

    @RequestMapping("del.do")
    @ResponseBody
    public ResponseService<String> del(HttpSession session,Integer shippingId){
        User current_user = (User) session.getAttribute(Const.CURRENT_USER);
        if (current_user != null){
            return shippingService.del(current_user.getId(),shippingId);
        }
        return ResponseService.creatByError("请登录");
    }

    @RequestMapping("select.do")
    @ResponseBody
    public ResponseService<Shipping> select(HttpSession session,Integer shippingId){
        User current_user = (User) session.getAttribute(Const.CURRENT_USER);
        if (current_user != null){
            return shippingService.select(current_user.getId(),shippingId);
        }
        return ResponseService.creatByError("请登录");
    }

    @RequestMapping("update.do")
    @ResponseBody
    public ResponseService<String> update(HttpSession session,Shipping shipping,Integer shippingId){
        User current_user = (User) session.getAttribute(Const.CURRENT_USER);
        if (current_user != null){
            return shippingService.update(current_user.getId(),shipping,shippingId);
        }
        return ResponseService.creatByError("请登录");
    }

    @RequestMapping("list_detail.do")
    @ResponseBody
    public ResponseService<PageInfo<Shipping>> listDetail(HttpSession session,
                                                          @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize,
                                                          @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum){
        User current_user = (User) session.getAttribute(Const.CURRENT_USER);
        if (current_user != null){
            return shippingService.listDetail(current_user.getId(),pageNum,pageSize);
        }
        return ResponseService.creatByError("请登录");
    }
}
