package com.mmall.service.impl;

import com.mmall.common.ResponseCodeEnum;
import com.mmall.common.ResponseService;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CategoryServiceImpl implements ICategoryService {
    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public ResponseService<List<Category>> getCategory(Integer categoryId) {
        if (categoryId == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }
        List<Category> categoryList = categoryMapper.selectByParentId(categoryId);
        if (CollectionUtils.isEmpty(categoryList)){
            logger.info("此商品没有子类");
        }
        return ResponseService.creatBySuccess(categoryList);
    }

    @Override
    public ResponseService<String> setCategory(Integer categoryId, String categoryName) {
        if (categoryId == null || categoryName == null || categoryName.equals(" ")){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        int result = categoryMapper.updateByPrimaryKeySelective(category);
        if (result > 0){
            return ResponseService.creatBySuccessMessage("更改名称成功");
        }
        return ResponseService.creatByError("更改失败");
    }

    @Override
    public ResponseService<String> addCategory(Integer categoryId, String categoryName) {
        if (categoryId == null || categoryName == null || categoryName.equals(" ")){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }
        Category category = new Category();
        category.setId(categoryId);
        category.setName(categoryName);
        category.setStatus(true);
        int result = categoryMapper.insert(category);
        if (result > 0){
            return ResponseService.creatBySuccessMessage("添加成功");
        }
        return ResponseService.creatByError("添加失败");
    }

    @Override
    public ResponseService<Set<Category>> getDeepCategory(Integer categoryId) {
        Set<Category> categorySet = new HashSet<>();
        getDeepCategory(categorySet, categoryId);
        return ResponseService.creatBySuccess(categorySet);
    }

    private void getDeepCategory(Set<Category> categories,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null){
            //商品添加到集合中
            categories.add(category);
        }
        //查询子商品
        List<Category> categoryList = categoryMapper.selectByParentId(categoryId);
        for (Category categoryItem : categoryList){
            //子商品继续递归找子商品的字商品
            getDeepCategory(categories,categoryItem.getId());
        }
    }
}
