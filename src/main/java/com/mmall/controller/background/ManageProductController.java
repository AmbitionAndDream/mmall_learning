package com.mmall.controller.background;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCodeEnum;
import com.mmall.common.ResponseService;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.FTPService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/manage/product")
public class ManageProductController {
    private final IProductService productService;
    private final IUserService userService;
    private final FTPService ftpService;

    public ManageProductController(IProductService productService, IUserService userService, FTPService ftpService) {
        this.productService = productService;
        this.userService = userService;
        this.ftpService = ftpService;
    }

    /**
     * 全部产品
     * @param session
     * @param pageNum 起始页
     * @param pageSize 页容量
     * @return
     */
    @RequestMapping("/list.do")
    @ResponseBody
    public ResponseService<PageInfo<ProductListVo>> list(HttpSession session,
                                                         @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                                         @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User current_user = (User)session.getAttribute(Const.CURRENT_USER);
        if (current_user == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),"请登录管理员");
        }
        if (userService.getRole(current_user).isSuccess()){
            return productService.list(pageNum,pageSize);
        }
        return ResponseService.creatByError("不是管理员不能操作");
    }

    /**
     * 根据产品名称或者id搜索产品
     * @param session
     * @param productName 名称
     * @param productId id
     * @param pageNum 起始页
     * @param pageSize 页容量
     * @return
     */
    @RequestMapping("/search.do")
    @ResponseBody
    public ResponseService<PageInfo<ProductListVo>> search(HttpSession session,
                                                          String productName,
                                                          Integer productId,
                                                          @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                                          @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User current_user = (User)session.getAttribute(Const.CURRENT_USER);
        if (current_user == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),"请登录管理员");
        }
        if (userService.getRole(current_user).isSuccess()){
            return productService.search(productName,productId,pageNum,pageSize);
        }
        return ResponseService.creatByError("不是管理员不能操作");
    }


    /**
     * 更新存储产品
     * @param session
     * @param product
     * @return
     */
    @RequestMapping("save.do")
    @ResponseBody
    public ResponseService<String> save(HttpSession session, Product product){
        User current_user = (User)session.getAttribute(Const.CURRENT_USER);
        if (current_user == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),"请登录管理员");
        }
        if (userService.getRole(current_user).isSuccess()){
            return productService.saveOrUpdateProduct(product);
        }
        return ResponseService.creatByError("不是管理员不能操作");
    }


    /**
     * 更改产品状态
     * @param session
     * @param productId
     * @param status
     * @return
     */
    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ResponseService<String> setSaleStatus(HttpSession session, Integer productId,Integer status){
        User current_user = (User)session.getAttribute(Const.CURRENT_USER);
        if (current_user == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),"请登录管理员");
        }
        if (userService.getRole(current_user).isSuccess()){
            return productService.setSaleStatus(productId,status);
        }
        return ResponseService.creatByError("不是管理员不能操作");
    }


    /**
     * 获得产品详情
     * @param session
     * @param productId
     * @return
     */
    @RequestMapping("detail.do")
    @ResponseBody
    public ResponseService<ProductDetailVo> getDetail(HttpSession session, Integer productId){
        User current_user = (User)session.getAttribute(Const.CURRENT_USER);
        if (current_user == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),"请登录管理员");
        }
        if (userService.getRole(current_user).isSuccess()){
            return productService.getDetail(productId);
        }
        return ResponseService.creatByError("不是管理员不能操作");
    }


    /**
     * 文件上传
     * @param session
     * @param file
     * @param request
     * @return
     */
    @RequestMapping("upload.do")
    @ResponseBody
    public ResponseService<Map<String, String>> upload(HttpSession session,
                                                       @RequestParam(value = "upload_file",required = false) MultipartFile file,
                                                       HttpServletRequest request){
        User current_user = (User)session.getAttribute(Const.CURRENT_USER);
        if (current_user == null){
            return ResponseService.createByErrorCodeMessage(ResponseCodeEnum.NEED_LOGIN.getCode(),"请登录管理员");
        }
        if(userService.getRole(current_user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = ftpService.upload(file,path);
            String url = PropertiesUtil.getProperties("ftp.server.http.prefix")+targetFileName;

            Map<String, String> map = Maps.newHashMap();
            map.put("uri",targetFileName);
            map.put("url",url);
            return ResponseService.creatBySuccess(map);
        }else{
            return ResponseService.creatByError("无权限操作");
        }
    }
}
