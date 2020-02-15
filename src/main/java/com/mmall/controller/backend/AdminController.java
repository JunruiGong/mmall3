package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * 管理员操作相关
 *
 * @Author: Junrui Gong
 * @Date: 2/15/20
 */
@Controller
@RequestMapping("/admin/")
public class AdminController {

    @Autowired
    IUserService iUserService;

    /**
     * 管理员登陆
     *
     * @param username    用户名
     * @param password    密码
     * @param httpSession httpSession
     * @return ServerResponse
     */
    @RequestMapping(value = "login.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession httpSession) {
        ServerResponse<User> response = iUserService.login(username, password);

        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.role_admin) {
                httpSession.setAttribute(Const.CURRENT_USER, user);
                return response;
            } else {
                return ServerResponse.createByErrorMessage("该账号非管理员");
            }
        } else {
            return response;
        }

    }
}
