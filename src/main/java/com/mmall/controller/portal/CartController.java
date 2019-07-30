package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseService;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import com.mmall.vo.CartProductVo;
import com.mmall.vo.CartVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {
    private final ICartService cartService;

    public CartController(ICartService cartService) {
        this.cartService = cartService;
    }


    @RequestMapping("/list.do")
    @ResponseBody
    public ResponseService<CartVo> list(HttpSession session){
        User current_user = (User) session.getAttribute(Const.CURRENT_USER);
        if (current_user != null){
            return cartService.list(current_user.getId());
        }
        return ResponseService.creatByError("请登录");
    }

    @RequestMapping("/add.do")
    @ResponseBody
    public ResponseService<CartVo> add(HttpSession session,Integer count,Integer productId){
        User current_user = (User) session.getAttribute(Const.CURRENT_USER);
        if (current_user != null){
            return cartService.add(current_user.getId(),count,productId);
        }
        return ResponseService.creatByError("请登录");
    }

    @RequestMapping("/update.do")
    @ResponseBody
    public ResponseService<CartVo> update(HttpSession session,Integer count,Integer productId){
        User current_user = (User) session.getAttribute(Const.CURRENT_USER);
        if (current_user != null){
            return cartService.update(current_user.getId(),count,productId);
        }
        return ResponseService.creatByError("请登录");
    }

    @RequestMapping("/delete.do")
    @ResponseBody
    public ResponseService<CartVo> delete(HttpSession session,String productIds){
        User current_user = (User) session.getAttribute(Const.CURRENT_USER);
        if (current_user != null){
            return cartService.delete(current_user.getId(),productIds);
        }
        return ResponseService.creatByError("请登录");
    }

    @RequestMapping("/get_cart_product_count.do")
    @ResponseBody
    public ResponseService<Integer> getCartProductCount(HttpSession session){
        User current_user = (User) session.getAttribute(Const.CURRENT_USER);
        if (current_user != null){
            return cartService.getCartProductCount(current_user.getId());
        }
        return ResponseService.creatByError("请登录");
    }

    @RequestMapping("select_all.do")
    @ResponseBody
    public ResponseService<CartVo> selectAll(HttpSession session){
        User current_user = (User) session.getAttribute(Const.CURRENT_USER);
        if (current_user != null){
            return cartService.selectOrUnSelect(current_user.getId(),null,Const.Cart.CHECKED);
        }
        return ResponseService.creatByError("请登录");
    }

    @RequestMapping("un_select_all.do")
    @ResponseBody
    public ResponseService<CartVo> unSelectAll(HttpSession session){
        User current_user = (User) session.getAttribute(Const.CURRENT_USER);
        if (current_user != null){
            return cartService.selectOrUnSelect(current_user.getId(),null,Const.Cart.UN_CHECKED);
        }
        return ResponseService.creatByError("请登录");
    }


    @RequestMapping("select.do")
    @ResponseBody
    public ResponseService<CartVo> select(HttpSession session,Integer productId){
        User current_user = (User) session.getAttribute(Const.CURRENT_USER);
        if (current_user != null){
            return cartService.selectOrUnSelect(current_user.getId(),productId,Const.Cart.CHECKED);
        }
        return ResponseService.creatByError("请登录");
    }

    @RequestMapping("un_select.do")
    @ResponseBody
    public ResponseService<CartVo> unSelect(HttpSession session,Integer productId){
        User current_user = (User) session.getAttribute(Const.CURRENT_USER);
        if (current_user != null){
            return cartService.selectOrUnSelect(current_user.getId(),productId,Const.Cart.UN_CHECKED);
        }
        return ResponseService.creatByError("请登录");
    }
}
