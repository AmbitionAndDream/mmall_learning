package com.mmall.controller.background;

import com.mmall.common.Const;
import com.mmall.common.ResponseService;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/manage/user")
public class UserManageController {

    private final IUserService iUserService;

    public UserManageController(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @RequestMapping(value="login.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseService<User> login(String username, String password, HttpSession session){
        ResponseService<User> response = iUserService.login(username,password);
        if(response.isSuccess()){
            User user = response.getData();
            if(user.getRole().equals(Const.Role.ROLE_ADMIN)){
                //说明登录的是管理员
                session.setAttribute(Const.CURRENT_USER,user);
                return response;
            }else{
                return ResponseService.creatByError("不是管理员,无法登录");
            }
        }
        return response;
    }

}
