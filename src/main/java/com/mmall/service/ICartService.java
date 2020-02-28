package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.vo.CartVO;

/**
 * @Author: Junrui Gong
 * @Date: 2/28/20
 */
public interface ICartService {

    ServerResponse<CartVO> addCart(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVO> updateCart(Integer userId, Integer productId, Integer count);

    ServerResponse<CartVO> deleteProduct(Integer userId, String productIds);

    ServerResponse<CartVO> listCart(Integer userId);

    ServerResponse<CartVO> selectAllOrUnSelect(Integer userId, Integer productId, int checked);
}
