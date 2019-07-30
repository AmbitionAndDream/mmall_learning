package com.mmall.service;

import com.mmall.common.ResponseService;
import com.mmall.vo.CartVo;

import java.util.List;

public interface ICartService {
    ResponseService<CartVo> list(Integer userId);

    ResponseService<CartVo> add(Integer userId, Integer count, Integer productId);

    ResponseService<CartVo> update(Integer userId, Integer count, Integer productId);

    ResponseService<CartVo> delete(Integer userId, String productIds);

    ResponseService<Integer> getCartProductCount(Integer userId);

    ResponseService<CartVo> selectOrUnSelect(Integer userId,Integer productId, Integer Checked);
}
