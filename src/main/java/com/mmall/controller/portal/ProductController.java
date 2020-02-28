package com.mmall.controller.portal;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.vo.ProductDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * @Author: Junrui Gong
 * @Date: 2/22/20
 */
@Controller
@RequestMapping("/product/")
public class ProductController {

    @Autowired
    private IProductService iProductService;


    @RequestMapping(value = "product_detail.do", method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<ProductDetailVO> getProductDetail(Integer productId) {

        return iProductService.getProductDetail(productId);
    }

    @RequestMapping("product_list.do")
    @ResponseBody
    public ServerResponse getProductList(@RequestParam(value = "keyword", required = false) String keyword,
                                         @RequestParam(value = "categoryId", required = false) Integer categoryId,
                                         @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                         @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                         @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        return iProductService.getProductListByKeywordAndCategoryId(keyword, categoryId, pageNum, pageSize, orderBy);
    }


}
