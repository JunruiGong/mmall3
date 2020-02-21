package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

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

    private IFileService iFileService;


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

    @RequestMapping("upload.do")
    @ResponseBody
    public ServerResponse upload(@RequestParam(value = "upload_file", required = false) MultipartFile file, HttpServletRequest request, HttpSession httpSession) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆");
        }

        // 校验是否为管理员
        ServerResponse<Integer> adminResponse = iUserService.isAdmin(user);
        if (!adminResponse.isSuccess()) {
            return adminResponse;
        } else {
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix") + targetFileName;

            Map fileMap = Maps.newHashMap();

            fileMap.put("uri", targetFileName);
            fileMap.put("url", url);
            return ServerResponse.createBySuccess(fileMap);
        }
    }


}
