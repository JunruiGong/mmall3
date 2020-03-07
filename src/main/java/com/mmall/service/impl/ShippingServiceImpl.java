package com.mmall.service.impl;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ShippingMapper;
import com.mmall.pojo.Shipping;
import com.mmall.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: Junrui Gong
 * @Date: 3/7/20
 */
@Service("iShippingService")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse add(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);

        int resultCount = shippingMapper.insert(shipping);

        if (resultCount > 0) {
            HashMap result = Maps.newHashMap();
            result.put("ShippingId", shipping.getId());
            return ServerResponse.createBySuccessMessage("新建地址成功", result);
        } else {
            return ServerResponse.createByErrorMessage("添加地址失败，请重试");
        }

    }

    @Override
    public ServerResponse delete(Integer userId, Integer shippingId) {

        int resultCount = shippingMapper.deleteByUserIdShippingId(userId, shippingId);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("删除地址成功");
        } else {
            return ServerResponse.createByErrorMessage("删除失败");
        }
    }

    @Override
    public ServerResponse update(Integer userId, Shipping shipping) {

        shipping.setUserId(userId);
        int resultCount = shippingMapper.updateByShipping(shipping);

        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("修改地址成功");
        } else {
            return ServerResponse.createByErrorMessage("修改地址失败");
        }
    }

    @Override
    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {

        Shipping shipping = shippingMapper.selectByUserIdShippingId(userId, shippingId);
        if (shipping == null) {
            return ServerResponse.createByErrorMessage("获取地址出错");
        } else {
            return ServerResponse.createBySuccess(shipping);
        }

    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        List<Shipping> shippingList = shippingMapper.selectShippingList(userId);

        PageInfo pageInfo = new PageInfo(shippingList);

        return ServerResponse.createBySuccess(pageInfo);
    }
}
