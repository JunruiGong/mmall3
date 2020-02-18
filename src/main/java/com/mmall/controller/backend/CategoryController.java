package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author: Junrui Gong
 * @Date: 2/18/20
 */
@Controller
@RequestMapping("/admin/category/")
public class CategoryController {

    @Autowired
    public IUserService iUserService;

    @Autowired
    public ICategoryService iCategoryService;


    @RequestMapping("add_category.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession httpSession,
                                      String categoryName,
                                      @RequestParam(value = "parentId", defaultValue = "0") int parentId) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆");
        }

        // 校验是否为管理员
        ServerResponse<Integer> adminResponse = iUserService.isAdmin(user);
        if (!adminResponse.isSuccess()) {
            return adminResponse;
        }

        return iCategoryService.addCategory(parentId, categoryName);
    }


    @RequestMapping("update_category_name.do")
    @ResponseBody
    public ServerResponse updateCategoryName(HttpSession httpSession,
                                             String categoryName, Integer categoryId) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆");
        }

        // 校验是否为管理员
        ServerResponse<Integer> adminResponse = iUserService.isAdmin(user);
        if (!adminResponse.isSuccess()) {
            return adminResponse;
        }

        return iCategoryService.updateCategoryName(categoryId, categoryName);


    }

    @RequestMapping("get_children_parallel_category.do")
    @ResponseBody
    public ServerResponse getChildrenParallelCategory(HttpSession httpSession,
                                                      @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆");
        }

        // 校验是否为管理员
        ServerResponse<Integer> adminResponse = iUserService.isAdmin(user);
        if (!adminResponse.isSuccess()) {
            return adminResponse;
        }

        // 查询当前节点的子节点及与该子节点平级的节点
        return iCategoryService.getChildrenParallelCategory(categoryId);

    }

    @RequestMapping("get_deep_children_category.do")
    @ResponseBody
    public ServerResponse getDeepChildrenCategory(HttpSession httpSession,
                                                  @RequestParam(value = "categoryId", defaultValue = "0") Integer categoryId) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆");
        }

        // 校验是否为管理员
        ServerResponse<Integer> adminResponse = iUserService.isAdmin(user);
        if (!adminResponse.isSuccess()) {
            return adminResponse;
        }

        // 查询当前节点的子节点及和该节点平级的节点
        return iCategoryService.getDeepChildrenCategory(categoryId);

    }


}
