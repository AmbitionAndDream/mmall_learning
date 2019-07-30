package com.mmall.service;

import com.mmall.common.ResponseService;
import com.mmall.pojo.Category;

import java.util.List;
import java.util.Set;

public interface ICategoryService {
    ResponseService<List<Category>> getCategory(Integer categoryId);

    ResponseService<String> setCategory(Integer categoryId, String categoryName);

    ResponseService<String> addCategory(Integer categoryId, String categoryName);

    ResponseService<Set<Category>> getDeepCategory(Integer categoryId);
}
