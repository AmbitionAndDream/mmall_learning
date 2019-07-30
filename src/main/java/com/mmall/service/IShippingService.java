package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseService;
import com.mmall.pojo.Shipping;

import java.util.Map;

public interface IShippingService {
    ResponseService<Map<String, Integer>> add(Integer UserId, Shipping shipping);

    ResponseService<String> del(Integer userId, Integer shippingId);

    ResponseService<Shipping> select(Integer userId, Integer shippingId);

    ResponseService<String> update(Integer userId, Shipping shipping, Integer shippingId);

    ResponseService<PageInfo<Shipping>> listDetail(Integer userId, Integer pageNum, Integer pageSize);
}
