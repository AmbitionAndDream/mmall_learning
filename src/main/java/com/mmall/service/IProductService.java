package com.mmall.service;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseService;
import com.mmall.pojo.Product;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;

import java.util.List;

public interface IProductService {
    ResponseService<PageInfo<ProductListVo>> list(Integer pageNum, Integer pageSize);

    ResponseService<PageInfo<ProductListVo>> search(String productName, Integer productId, Integer pageNum, Integer pageSize);

    ResponseService<String> saveOrUpdateProduct(Product product);

    ResponseService<String> setSaleStatus(Integer productId, Integer status);

    ResponseService<ProductDetailVo> getDetail(Integer productId);

    ResponseService<PageInfo<ProductListVo>> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy);
}
