package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCodeEnum;
import com.mmall.common.ResponseService;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements IProductService {
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;

    public ProductServiceImpl(ProductMapper productMapper, CategoryMapper categoryMapper) {
        this.productMapper = productMapper;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public ResponseService<PageInfo<ProductListVo>> list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Product> productList = productMapper.selectList();
        return getPageInfoResponseService(productList);
    }

    @Override
    public ResponseService<PageInfo<ProductListVo>> search(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        if(StringUtils.isNotBlank(productName)){
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        }
        List<Product> productList = productMapper.selectByProductNameOrProductId(productName,productId);
        return getPageInfoResponseService(productList);
    }

    @Override
    public ResponseService<String> saveOrUpdateProduct(Product product) {
        if (product != null){
            if(StringUtils.isNotBlank(product.getSubImages())){
                String[] subImageArray = product.getSubImages().split(",");
                if(subImageArray.length > 0){
                    product.setMainImage(subImageArray[0]);
                }
            }
            if (product.getId() != null){
                int result = productMapper.updateByPrimaryKey(product);
                if (result > 0){
                    return ResponseService.creatBySuccessMessage("更新成功");
                }
                return ResponseService.creatByError("更新失败");
            }else {
                int result = productMapper.insert(product);
                if (result > 0){
                    return ResponseService.creatBySuccessMessage("添加成功");
                }
                return ResponseService.creatByError("添加失败");
            }
        }
        return ResponseService.creatByError("新增产品信息不正确");
    }

    @Override
    public ResponseService<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int result = productMapper.updateByPrimaryKeySelective(product);
        if (result > 0){
            return ResponseService.creatBySuccessMessage("商品状态更新成功");
        }
        return ResponseService.creatByError("商品状态更新失败");
    }

    @Override
    public ResponseService<ProductDetailVo> getDetail(Integer productId) {
        if (productId == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),"参数错误");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product == null){
            return ResponseService.creatByError("产品已下架或者删除");
        }
        ProductDetailVo productDetailVo = assembleProductDetailVo(product);
        return ResponseService.creatBySuccess(productDetailVo);
    }

    public ResponseService<PageInfo<ProductListVo>> getProductByKeywordCategory(String keyword,Integer categoryId,int pageNum,int pageSize,String orderBy){
        if(StringUtils.isBlank(keyword) && categoryId == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.ILLEGAL_ARGUMENT.getCode(),ResponseCodeEnum.ILLEGAL_ARGUMENT.getDesc());
        }
        List<Category> categoryIdList = new ArrayList<>();
        if(categoryId != null){
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if(category == null && StringUtils.isBlank(keyword)){
                //没有该分类,并且还没有关键字,这个时候返回一个空的结果集,不报错
                PageHelper.startPage(pageNum,pageSize);
                List<ProductListVo> productListVoList = Lists.newArrayList();
                PageInfo<ProductListVo> pageInfo = new PageInfo<>();
                pageInfo.setList(productListVoList);
                return ResponseService.creatBySuccess(pageInfo);
            }
            categoryIdList = categoryMapper.selectByParentId(category.getId());
        }
        if(StringUtils.isNotBlank(keyword)){
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        }

        PageHelper.startPage(pageNum,pageSize);
        //排序处理
        if(StringUtils.isNotBlank(orderBy)){
            if(Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)){
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0]+" "+orderByArray[1]);
            }
        }
        List<Product> productList = productMapper.selectByNameAndCategoryIds(StringUtils.isBlank(keyword)?null:keyword,categoryIdList.size()==0?null:categoryIdList);

        List<ProductListVo> productListVoList = Lists.newArrayList();
        for(Product product : productList){
            ProductListVo productListVo = assembleProductListVo(product);
            productListVoList.add(productListVo);
        }
        PageInfo<ProductListVo> pageInfo = new PageInfo<>();
        pageInfo.setList(productListVoList);
        return ResponseService.creatBySuccess(pageInfo);
    }


    private ProductDetailVo assembleProductDetailVo(Product product) {
        ProductDetailVo productDetailVo = new ProductDetailVo();

        productDetailVo.setId(product.getId());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setPrice(product.getPrice());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setName(product.getName());
        productDetailVo.setStatus(product.getStatus());
        productDetailVo.setStock(product.getStock());
        productDetailVo.setImageHost(PropertiesUtil.getProperties("ftp.server.http.prefix","http://www.xiexyh29.com.cn/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if(category == null){
            productDetailVo.setParentCategoryId(0);//默认根节点
        }else{
            productDetailVo.setParentCategoryId(category.getParentId());
        }

        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    private ResponseService<PageInfo<ProductListVo>> getPageInfoResponseService(List<Product> productList) {
        List<ProductListVo> productListVoList = Lists.newArrayList();
        for (Product productItem : productList){
            ProductListVo productListVo = assembleProductListVo(productItem);
            productListVoList.add(productListVo);
        }
        PageInfo<ProductListVo> pageResult = new PageInfo<>(productListVoList);
        pageResult.setList(productListVoList);
        return ResponseService.creatBySuccess(pageResult);
    }

    private ProductListVo assembleProductListVo(Product productItem) {
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(productItem.getId());
        productListVo.setCategoryId(productItem.getCategoryId());
        productListVo.setName(productItem.getName());
        productListVo.setMainImage(productItem.getMainImage());
        productListVo.setImageHost(PropertiesUtil.getProperties("ftp.server.http.prefix","http://www.xiexyh29.com.cn/"));
        productListVo.setPrice(productItem.getPrice());
        productListVo.setSubtitle(productItem.getSubtitle());
        productListVo.setStatus(productItem.getStatus());
        return productListVo;
    }
}
