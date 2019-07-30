package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.ResponseService;
import com.mmall.pojo.PayInfo;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product/")
public class ProductController {
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @RequestMapping("detail.do")
    @ResponseBody
    public ResponseService<ProductDetailVo> detail(Integer productId){
        return productService.getDetail(productId);
    }

    @RequestMapping("list.do")
    @ResponseBody
    public ResponseService<PageInfo<ProductListVo>> list(@RequestParam(value = "keyword",required = false)String keyword,
                                                         @RequestParam(value = "categoryId",required = false)Integer categoryId,
                                                         @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                                         @RequestParam(value = "pageSize",defaultValue = "10") int pageSize,
                                                         @RequestParam(value = "orderBy",defaultValue = "") String orderBy){
        return productService.getProductByKeywordCategory(keyword,categoryId,pageNum,pageSize,orderBy);
    }
}
