package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
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
 * 消费者操作相关
 * @Author: Junrui Gong
 * @Date: 2/14/20
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    /**
     * 用户登陆
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
            httpSession.setAttribute(Const.CURRENT_USER, response.getData());
        }

        return response;
    }

    /**
     * 退出登陆
     *
     * @param httpSession httpSession
     * @return ServerResponse
     */
    @RequestMapping(value = "logout.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> logout(HttpSession httpSession) {

        httpSession.removeAttribute(Const.CURRENT_USER);

        return ServerResponse.createBySuccess();
    }

    /**
     * 注册
     *
     * @param user user
     * @return ServerResponse
     */
    @RequestMapping(value = "register.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> register(User user) {
        return iUserService.register(user);
    }


    /**
     * 校验用户名或邮箱是否存在
     *
     * @param str  username/email
     * @param type username/email
     * @return ServerResponse
     */
    @RequestMapping(value = "check_valid.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkValid(String str, String type) {
        return iUserService.checkValid(str, type);

    }

    /**
     * 获取当前登陆的用户信息
     *
     * @param httpSession httpSession
     * @return ServerResponse
     */
    @RequestMapping(value = "get_user_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getUserInfo(HttpSession httpSession) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user != null) {
            return ServerResponse.createBySuccess(user);
        }

        return ServerResponse.createByErrorMessage("用户未登陆");

    }

    /**
     * 忘记密码，获取密码重置问题
     *
     * @param username 用户名
     * @return ServerResponse
     */
    @RequestMapping(value = "forget_question.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetGetQuestion(String username) {
        return iUserService.forgetGetQuestion(username);
    }

    /**
     * 忘记密码，检查答案是否正确
     *
     * @param username username
     * @param question question
     * @param answer   answer
     * @return ServerResponse
     */
    @RequestMapping(value = "check_answer.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        return iUserService.checkAnswer(username, question, answer);
    }

    /**
     * 重置密码
     *
     * @param username    username
     * @param newPassword newPassword
     * @param forgetToken forgetToken
     * @return ServerResponse
     */
    @RequestMapping(value = "rest_password.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> RestPassword(String username, String newPassword, String forgetToken) {
        return iUserService.restPassword(username, newPassword, forgetToken);
    }

    /**
     * 更新个人信息
     *
     * @param user        user
     * @param httpSession httpSession
     * @return ServerResponse
     */
    @RequestMapping(value = "update_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> updateInfo(User user, HttpSession httpSession) {

        User currentUser = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorMessage("用户未登陆");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> updateResponse = iUserService.updateInfo(user);

        if (updateResponse.isSuccess()) {
            httpSession.setAttribute(Const.CURRENT_USER, updateResponse.getData());
        }
        return updateResponse;
    }


    /**
     * 获取个人信息
     *
     * @param httpSession httpSession
     * @return ServerResponse
     */
    @RequestMapping(value = "get_info.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> getInfo(HttpSession httpSession) {
        User currentUser = (User) httpSession.getAttribute(Const.CURRENT_USER);
        if (currentUser == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登陆");
        }

        return iUserService.getInfo(currentUser.getId());
    }
}
