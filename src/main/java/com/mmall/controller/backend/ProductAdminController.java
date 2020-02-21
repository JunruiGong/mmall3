package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author: Junrui Gong
 * @Date: 2/18/20
 */
@Controller
@RequestMapping("/admin/product/")
public class ProductAdminController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private IProductService iProductService;


    @RequestMapping("save.do")
    @ResponseBody
    public ServerResponse saveProduct(HttpSession httpSession, Product product) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆");
        }

        // 校验是否为管理员
        ServerResponse<Integer> adminResponse = iUserService.isAdmin(user);
        if (!adminResponse.isSuccess()) {
            return adminResponse;
        } else {
            return iProductService.saveOrUpdateProduct(product);
        }
    }


    @RequestMapping("set_sale_status.do")
    @ResponseBody
    public ServerResponse setSaleStatus(HttpSession httpSession, Integer productId, Integer status) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆");
        }

        // 校验是否为管理员
        ServerResponse<Integer> adminResponse = iUserService.isAdmin(user);
        if (!adminResponse.isSuccess()) {
            return adminResponse;
        } else {
            return iProductService.setSaleStatus(productId, status);
        }
    }


    @RequestMapping("product_detail.do")
    @ResponseBody
    public ServerResponse getProductDetail(HttpSession httpSession, Integer productId) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆");
        }

        // 校验是否为管理员
        ServerResponse<Integer> adminResponse = iUserService.isAdmin(user);
        if (!adminResponse.isSuccess()) {
            return adminResponse;
        } else {
            return iProductService.getProductDetail(productId);
        }
    }

    @RequestMapping("product_list.do")
    @ResponseBody
    public ServerResponse getProductList(HttpSession httpSession,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆");
        }

        // 校验是否为管理员
        ServerResponse<Integer> adminResponse = iUserService.isAdmin(user);
        if (!adminResponse.isSuccess()) {
            return adminResponse;
        } else {
            return iProductService.getProductList(pageNum, pageSize);
        }
    }

    @RequestMapping("search.do")
    @ResponseBody
    public ServerResponse searchProduct(HttpSession httpSession, String productName, Integer productId,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                        @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆");
        }

        // 校验是否为管理员
        ServerResponse<Integer> adminResponse = iUserService.isAdmin(user);
        if (!adminResponse.isSuccess()) {
            return adminResponse;
        } else {
            return iProductService.searchProduct(productName, productId, pageNum, pageSize);
        }
    }


}
