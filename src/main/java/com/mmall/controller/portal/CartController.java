package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * @Author: Junrui Gong
 * @Date: 2/28/20
 */
@Controller
@RequestMapping("/cart/")
public class CartController {


    @Autowired
    private ICartService iCartService;

    @RequestMapping(value = "add.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse addCart(HttpSession httpSession, Integer count, Integer productId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.addCart(user.getId(), productId, count);
    }

    @RequestMapping(value = "update.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse updateCart(HttpSession httpSession, Integer count, Integer productId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.updateCart(user.getId(), productId, count);
    }


    @RequestMapping(value = "delete_product.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse deleteProduct(HttpSession httpSession, String productIds) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.deleteProduct(user.getId(), productIds);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse listCart(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.listCart(user.getId());
    }


    @RequestMapping(value = "select_all.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse selectAll(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectAllOrUnSelect(user.getId(), null, Const.Cart.CHECKED);
    }

    @RequestMapping(value = "un_select_all.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse unSelectAll(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectAllOrUnSelect(user.getId(), null, Const.Cart.UN_CHECKED);
    }


    @RequestMapping(value = "select.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse select(HttpSession httpSession, Integer productId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectAllOrUnSelect(user.getId(), productId, Const.Cart.CHECKED);
    }

    @RequestMapping(value = "un_select.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse unSelect(HttpSession httpSession, Integer productId) {
        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iCartService.selectAllOrUnSelect(user.getId(), productId, Const.Cart.UN_CHECKED);
    }

}
