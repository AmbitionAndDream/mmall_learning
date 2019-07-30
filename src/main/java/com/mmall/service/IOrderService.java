package com.mmall.service;

import com.mmall.common.ResponseService;

import java.util.Map;

public interface IOrderService {
    ResponseService pay(Integer userId, Long orderNo, String path);

    ResponseService queryOrderPayStatus(Integer userId, Long orderNo);

    ResponseService aliCallback(Map<String, String> params);
}
