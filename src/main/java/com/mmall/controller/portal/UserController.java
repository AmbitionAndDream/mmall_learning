package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCodeEnum;
import com.mmall.common.ResponseService;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    private final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }
    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @param session 会话
     * @return 响应对象
     */
    @RequestMapping(value = {"/login.do"},method = RequestMethod.POST)
    @ResponseBody
    public ResponseService<User> login( String username, String password, HttpSession session){
        ResponseService<User> responseService = userService.login(username, password);
        if (responseService.isSuccess()){
            session.setAttribute(Const.CURRENT_USER,responseService.getData());
        }
        return responseService;
    }

    /**
     * 注册
     * @param user 数据绑定
     * @return 响应对象
     */
    @RequestMapping(value = {"/register.do"},method = RequestMethod.POST)
    @ResponseBody
    public ResponseService<String> register(User user){
        return userService.register(user);
    }

    /**
     * 获取用户信息
     * @param session 会话
     * @return 响应对象
     */
    @RequestMapping(value = {"/getInfor.do"},method = RequestMethod.GET)
    @ResponseBody
    public ResponseService<User> getInfor(HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if (currentUser == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),"需要登录");
        }
        return userService.getInformation(currentUser.getId());
    }

    /**
     * 忘记密码获取问题
     * @param username 用户名
     * @return 响应对象
     */
    @RequestMapping(value = {"/forget_get_question.do"},method = RequestMethod.POST)
    @ResponseBody
    public ResponseService<String> forget_get_question(String username){
        System.out.println(username);
        return userService.selectQuestion(username);
    }

    /**
     * 重置密码的问题以及答案（未登录）
     * @param username 用户名
     * @param question 问题
     * @param answer 答案
     * @return 响应对象
     */
    @RequestMapping(value = {"/checkAnswer.do"},method = RequestMethod.POST)
    @ResponseBody
    public ResponseService<String> checkAnswer(String username,String question,String answer){
        return userService.checkAnswer(username,question,answer);
    }

    /**
     * 重置密码
     * @param username 用户名
     * @param passwordNew 新密码
     * @param forgetToken 返回重置密码答案的token
     * @return 响应对象
     */
    @RequestMapping(value = "forgetRestPassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseService<String> forgetRestPassword(String username,String passwordNew,String forgetToken){
        return userService.forgetResetPassword(username,passwordNew,forgetToken);
    }

    /**
     * 登录状态下重置密码
     * @param session 当前会话
     * @param passwordOld 旧密码
     * @param passwordNew 新密码
     * @return 响应对象
     */
    @RequestMapping(value = "/RestPassword.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseService<String> RestPassword(HttpSession session,String passwordOld,String passwordNew){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ResponseService.creatByError("需要登录");
        }
        return userService.ResetPassword(passwordOld,passwordNew,user);
    }

    /**
     * 登录状态下获取用户信息
     * @param session 当前会话
     * @param user 修改信息后的数据绑定User
     * @return 响应对象
     */
    @RequestMapping(value = "/update_information.do",method = RequestMethod.POST)
    @ResponseBody
    public ResponseService<User> update_information(HttpSession session,User user){
        User current_user = (User) session.getAttribute(Const.CURRENT_USER);
        if (current_user == null){
            return ResponseService.creatByError("需要登录");
        }
        user.setId(current_user.getId());
        ResponseService<User> responseService = userService.update_information(user);
        if(responseService.isSuccess()){
            responseService.getData().setUsername(current_user.getUsername());
            session.setAttribute(Const.CURRENT_USER,responseService.getData());
        }
        return responseService;
    }

}
