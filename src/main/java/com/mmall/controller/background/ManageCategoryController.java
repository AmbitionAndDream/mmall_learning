package com.mmall.controller.background;

import com.mmall.common.Const;
import com.mmall.common.ResponseCodeEnum;
import com.mmall.common.ResponseService;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/manage/category")
public class ManageCategoryController {
    private final ICategoryService categoryService;
    private final IUserService userService;

    public ManageCategoryController(ICategoryService categoryService, IUserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    /**
     * 获取商品信息（平级）
     * @param session
     * @param categoryId 父节点的id
     * @return
     */
    @RequestMapping(value = "getCategory.do",method = RequestMethod.GET)
    @ResponseBody
    public ResponseService<List<Category>> getCategory(HttpSession session, @RequestParam(value ="categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),"需要登录");
        }
        if (userService.getRole(user).isSuccess()){
            return categoryService.getCategory(categoryId);
        }else {
            return ResponseService.creatByError("非管理员，无操作权限");
        }
    }

    /**
     * 修改商品名字
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "setCategory.do",method = RequestMethod.GET)
    @ResponseBody
    public ResponseService<String> setCategory(HttpSession session, @RequestParam(value ="categoryId") Integer categoryId,@RequestParam("categoryName")String categoryName){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),"需要登录");
        }
        if (userService.getRole(user).isSuccess()){
            return categoryService.setCategory(categoryId,categoryName);
        }else {
            return ResponseService.creatByError("非管理员，无操作权限");
        }
    }

    /**
     * 添加商品
     * @param session
     * @param categoryId
     * @param categoryName
     * @return
     */
    @RequestMapping(value = "addCategory.do",method = RequestMethod.GET)
    @ResponseBody
    public ResponseService<String> addCategory(HttpSession session, @RequestParam(value ="categoryId",defaultValue = "0") Integer categoryId,@RequestParam("categoryName") String categoryName){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),"需要登录");
        }
        if (userService.getRole(user).isSuccess()){
            return categoryService.addCategory(categoryId,categoryName);
        }else {
            return ResponseService.creatByError("非管理员，无操作权限");
        }
    }

    /**
     * 获取当前商品信息以及子商品信息
     * @param session
     * @param categoryId
     * @return
     */
    @RequestMapping(value = "getDeepCategory.do",method = RequestMethod.GET)
    @ResponseBody
    public ResponseService<Set<Category>> getDeepCategory(HttpSession session, @RequestParam(value ="categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),"需要登录");
        }
        if (userService.getRole(user).isSuccess()){
            return categoryService.getDeepCategory(categoryId);
        }else {
            return ResponseService.creatByError("非管理员，无操作权限");
        }
    }
}
