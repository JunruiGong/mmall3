package com.mmall.controller.portal;

import com.github.pagehelper.PageInfo;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Shipping;
import com.mmall.pojo.User;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.time.temporal.ValueRange;

/**
 * @Author: Junrui Gong
 * @Date: 3/7/20
 */
@Controller
@RequestMapping("/shipping")
public class ShippingController {
    @Autowired
    private IShippingService iShippingService;


    @RequestMapping(value = "add.do", method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse addAddress(HttpSession httpSession, Shipping shipping) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iShippingService.add(user.getId(), shipping);
    }

    @RequestMapping(value = "delete.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse deleteAddress(HttpSession httpSession, Integer shippingId) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iShippingService.delete(user.getId(), shippingId);
    }

    @RequestMapping(value = "update.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse updateAddress(HttpSession httpSession, Shipping shipping) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iShippingService.update(user.getId(), shipping);
    }

    @RequestMapping(value = "select.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<Shipping> selectAddress(HttpSession httpSession, Integer shippingId) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }


        return iShippingService.select(user.getId(), shippingId);
    }

    @RequestMapping(value = "list.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<PageInfo> listAddress(HttpSession httpSession,
                                                @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {

        User user = (User) httpSession.getAttribute(Const.CURRENT_USER);

        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), ResponseCode.NEED_LOGIN.getDesc());
        }

        return iShippingService.list(user.getId(), pageNum, pageSize);
    }

}
