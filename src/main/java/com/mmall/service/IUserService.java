package com.mmall.service;

import com.mmall.common.ResponseService;
import com.mmall.pojo.User;

public interface IUserService {
    ResponseService<User> login(String username, String password);

    ResponseService<String> register(User user);

    ResponseService<String> selectQuestion(String username);

    ResponseService<String> checkAnswer(String username, String question, String answer);

    ResponseService<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ResponseService<String> ResetPassword(String passwordOld, String passwordNew, User user);

    ResponseService<User> update_information(User user);

    ResponseService<User> getInformation(Integer id);

    ResponseService getRole(User user);
}
