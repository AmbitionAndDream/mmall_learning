package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ResponseService;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ShippingServiceImpl implements IShippingService {
    private final ShippingMapper shippingMapper;

    public ShippingServiceImpl(ShippingMapper shippingMapper) {
        this.shippingMapper = shippingMapper;
    }

    @Override
    public ResponseService<Map<String, Integer>> add(Integer UserId, Shipping shipping) {
        shipping.setUserId(UserId);
        int result = shippingMapper.insert(shipping);
        if (result > 0){
            Map<String,Integer> shippingMap = Maps.newHashMap();
            shippingMap.put("shippingId",shipping.getId());
            return ResponseService.creatBySuccess("添加成功",shippingMap);
        }
        return ResponseService.creatByError("添加失败");
    }

    @Override
    public ResponseService<String> del(Integer userId, Integer shippingId) {
        int result = shippingMapper.deleteByShippingIdAndUserId(shippingId,userId);
        if (result > 0){
            return ResponseService.creatBySuccess("删除成功");
        }
        return ResponseService.creatByError("删除失败");
    }

    @Override
    public ResponseService<Shipping> select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId,shippingId);
        if(shipping == null){
            return ResponseService.creatByError("无法查询到该地址");
        }
        return ResponseService.creatBySuccess("查询地址成功",shipping);
    }

    @Override
    public ResponseService<String> update(Integer userId, Shipping shipping, Integer shippingId) {
        shipping.setUserId(userId);
        int result = shippingMapper.updateByShipping(shipping);
        if(result > 0){
            return ResponseService.creatByError("地址更新成功");
        }
        return ResponseService.creatBySuccess("地址更新失败");
    }

    @Override
    public ResponseService<PageInfo<Shipping>> listDetail(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        PageInfo<Shipping> shippingPageInfo = new PageInfo<>();
        shippingPageInfo.setList(shippingList);
        return ResponseService.creatBySuccess(shippingPageInfo);
    }
}
