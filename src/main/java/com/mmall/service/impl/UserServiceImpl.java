package com.mmall.service.impl;

import com.mmall.common.*;
import com.mmall.dao.UserMapper;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    private final UserMapper userMapper;
    private final UserInforCache userInforCache = new UserInforCache();

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public ResponseService<User> login(String username, String password) {
        //检查用户名
        int result = userMapper.checkByUsername(username);
        if (result == 0 ){
            return ResponseService.creatByError("用户名不存在");
        }
        //登录
        User user = userMapper.login(username,password);
        if (user == null){
            return ResponseService.creatByError("密码错误");
        }
        userInforCache.setValues(user.getId(),user);
        return ResponseService.creatBySuccess(user);
    }

    @Override
    public ResponseService<String> register(User user){
        ResponseService<String> checkVal = this.checkVal(user.getUsername(), Const.USERNAME);
        if (!checkVal.isSuccess()){
            return checkVal;
        }
        checkVal = this.checkVal(user.getEmail(), Const.EMAIL);
        if (!checkVal.isSuccess()){
            return checkVal;
        }
        //普通用户
        user.setRole(Const.Role.ROLE_CUSTOMER);
        int insert = userMapper.insert(user);
        if (insert > 0){
            return ResponseService.creatBySuccessMessage("注册成功");
        }
        return ResponseService.creatByError("注册失败");
    }

    private ResponseService<String> checkVal(String str, String type) {
        //校验用户名
        if (Const.USERNAME.equals(type)){
            int result = userMapper.checkByUsername(str);
            if (result > 0){
                return ResponseService.creatByError("用户名已存在");
            }
        }
        //校验邮箱
        if (Const.EMAIL.equals(type)){
            int result = userMapper.checkByEmail(str);
            if (result > 0){
                return ResponseService.creatByError("邮箱已存在");
            }
        }
        return ResponseService.creatBySuccess();
    }

    @Override
    public ResponseService<String> selectQuestion(String username){
        //检查用户名是否存在
        ResponseService<String> stringResponseService = this.checkVal(username, Const.USERNAME);
        if (stringResponseService.isSuccess()){
            return ResponseService.creatByError("用户名不存在");
        }
        //用户名存在
        String question = userMapper.selectQuestion(username);
        if (question == null){
            return ResponseService.creatByError("找回密码问题未设置");
        }
        return ResponseService.creatBySuccess(question);
    }

    @Override
    public ResponseService<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if (resultCount > 0){
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username,forgetToken);
            return ResponseService.creatBySuccess(forgetToken);
        }
        return ResponseService.creatByError("问题的答案错误");
    }

    @Override
    public ResponseService<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {
        if (forgetToken == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),"token参数错误");
        }
        ResponseService<String> stringResponseService = this.checkVal(username, Const.USERNAME);
        if(stringResponseService.isSuccess()){
            //用户不存在
            return ResponseService.creatByError("用户不存在");
        }
        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        if (token == null){
            return ResponseService.creatByError("token存活时间已过,需要重新验证重置密码的答案");
        }
        if(org.apache.commons.lang3.StringUtils.equals(forgetToken,token)){
            int rowCount = userMapper.updatePasswordByUsername(username,passwordNew);
            if(rowCount > 0){
                return ResponseService.creatBySuccessMessage("修改密码成功");
            }
        }else{
            return ResponseService.creatByError("token错误,请重新获取重置密码的token");
        }
        return ResponseService.creatByError("修改密码失败");
    }

    @Override
    public ResponseService<String> ResetPassword(String passwordOld, String passwordNew, User user) {
        int resultCount = userMapper.checkPassword(passwordOld,user.getId());
        if(resultCount == 0){
            return ResponseService.creatByError("旧密码错误");
        }
        user.setPassword(passwordNew);
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if(updateCount > 0){
            return ResponseService.creatBySuccessMessage("密码更新成功");
        }
        return ResponseService.creatByError("密码更新失败");
    }

    @Override
    public ResponseService<User> update_information(User user) {
        User updateUser = new User();

        updateUser.setId(user.getId());
        updateUser.setPhone(user.getPhone());
        updateUser.setEmail(user.getEmail());
        updateUser.setQuestion(user.getQuestion());
        updateUser.setAnswer(user.getAnswer());

        int count = userMapper.updateByPrimaryKeySelective(updateUser);
        if (count > 0){
            return ResponseService.creatBySuccess(user);
        }
        return ResponseService.creatByError("更新数据失败");
    }

    @Override
    public ResponseService<User> getInformation(Integer id) {
        User user = userInforCache.getValues(id);
        if (user == null){
            return ResponseService.creatByError("没有这个人");
        }
        return ResponseService.creatBySuccess(user);
    }

    //管理员
    public  ResponseService getRole(User user){
        if (user.getRole().equals(Const.Role.ROLE_ADMIN)){
            return ResponseService.creatBySuccess();
        }else {
            return ResponseService.creatByError();
        }
    }
}
