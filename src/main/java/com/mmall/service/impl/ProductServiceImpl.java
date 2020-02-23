package com.mmall.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.ICategoryService;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVO;
import com.mmall.vo.ProductListVO;
import com.sun.webkit.perf.PerfLogger;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.tools.asm.Assembler;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Junrui Gong
 * @Date: 2/18/20
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ICategoryService iCategoryService;


    @Override
    public ServerResponse saveOrUpdateProduct(Product product) {

        if (product == null) {
            return ServerResponse.createByErrorMessage("添加或更新产品参数不正确，请重试!");
        } else {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] subImageArray = product.getSubImages().split(",");

                if (subImageArray.length > 0) {
                    product.setMainImage(subImageArray[0]);
                }
            }

            if (product.getId() != null) {
                int resultCount = productMapper.updateByPrimaryKey(product);
                if (resultCount > 0) {
                    return ServerResponse.createBySuccessMessage("更新产品成功");
                } else {
                    return ServerResponse.createByErrorMessage("更新产品失败 ");
                }
            } else {
                int resultCount = productMapper.insert(product);
                if (resultCount > 0) {
                    return ServerResponse.createBySuccessMessage("添加产品成功");
                } else {
                    return ServerResponse.createByErrorMessage("添加产品失败 ");
                }
            }
        }
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        if (productId == null || status == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = new Product();
        product.setStatus(status);
        product.setId(productId);

        int resultCount = productMapper.updateByPrimaryKeySelective(product);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("修改商品状态成功");
        } else {
            return ServerResponse.createByErrorMessage("修改商品状态失败");
        }
    }

    @Override
    public ServerResponse<ProductDetailVO> getProductDetailManage(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("商品已下架或不存在");
        }

        ProductDetailVO productDetailVO = assembleProductVO(product);
        return ServerResponse.createBySuccess(productDetailVO);
    }

    @Override
    public ServerResponse<PageInfo> getProductList(int pageNum, int pageSize) {

        PageHelper.startPage(pageNum, pageSize);

        List<Product> productList = productMapper.selectProductList();

        List<ProductListVO> productListVOList = new ArrayList<>();

        for (Product productItem : productList) {
            ProductListVO productListVO = assembleProductListVO(productItem);
            productListVOList.add(productListVO);
        }

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVOList);

        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<PageInfo> searchProduct(String productName, Integer productId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (StringUtils.isNotBlank(productName)) {
            productName = "%" + productName + "%";
        }

        List<Product> productList = productMapper.selectByProductNameAndProductId(productName, productId);

        List<ProductListVO> productListVOList = new ArrayList<>();

        for (Product productItem : productList) {
            ProductListVO productListVO = assembleProductListVO(productItem);
            productListVOList.add(productListVO);
        }

        PageInfo pageResult = new PageInfo(productList);
        pageResult.setList(productListVOList);

        return ServerResponse.createBySuccess(pageResult);
    }

    @Override
    public ServerResponse<ProductDetailVO> getProductDetail(Integer productId) {
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null) {
            return ServerResponse.createByErrorMessage("商品已下架或不存在");
        }

        if (product.getStatus() != Const.ProductStatusEnum.ON_SALE.getCode()) {
            return ServerResponse.createByErrorMessage("商品已下架或不存在");
        }

        ProductDetailVO productDetailVO = assembleProductVO(product);
        return ServerResponse.createBySuccess(productDetailVO);
    }

    @Override
    public ServerResponse<PageInfo> getProductListByKeywordAndCategoryId(String keyword, Integer categoryId, int pageNum, int pageSize, String orderBy) {
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }

        List<Integer> categoryIdList = new ArrayList<>();


        if (categoryId != null) {
            Category category = categoryMapper.selectByPrimaryKey(categoryId);
            if (category == null && StringUtils.isBlank(keyword)) {
                // 返回空的结果集，不报错
                PageHelper.startPage(pageNum, pageSize);
                List<ProductListVO> productListVOList = Lists.newArrayList();
                PageInfo pageInfo = new PageInfo(productListVOList);
                return ServerResponse.createBySuccess(pageInfo);
            }
            categoryIdList = iCategoryService.getDeepChildrenCategory(category.getId()).getData();
        }

        if (StringUtils.isNotBlank(keyword)) {
            keyword = "%" + keyword + "%";
        }

        // 排序处理

        PageHelper.startPage(pageNum, pageSize);

        if (StringUtils.isNotBlank(orderBy)) {
            if (Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {
                String[] orderByArray = orderBy.split("_");
                PageHelper.orderBy(orderByArray[0] + " " + orderByArray[1]);
            }
        }

        List<Product> productList = productMapper.selectByProductNameAndCategoryIds(StringUtils.isBlank(keyword) ? null : keyword, categoryIdList.size() == 0 ? null : categoryIdList);

        List<ProductListVO> productListVOList = Lists.newArrayList();

        for (Product product : productList
        ) {
            ProductListVO productListVO = assembleProductListVO(product);
            productListVOList.add(productListVO);
        }

        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productListVOList);

        return ServerResponse.createBySuccess(pageInfo);
    }


    private ProductDetailVO assembleProductVO(Product product) {

        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setCategoryId(product.getId());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setSubImages(product.getSubImages());
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setDetail(product.getDetail());
        productDetailVO.setName(product.getName());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));

        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());

        if (category == null) {
            productDetailVO.setParentCategoryId(0);
        } else {
            productDetailVO.setParentCategoryId(category.getParentId());
        }

        productDetailVO.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVO.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));

        return productDetailVO;
    }


    private ProductListVO assembleProductListVO(Product product) {

        ProductListVO productListVO = new ProductListVO();
        productListVO.setCategoryId(product.getId());
        productListVO.setSubtitle(product.getSubtitle());
        productListVO.setPrice(product.getPrice());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setName(product.getName());
        productListVO.setStatus(product.getStatus());
        productListVO.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix", "http://img.happymmall.com/"));

        return productListVO;
    }


}
