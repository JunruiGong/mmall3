package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @Author: Junrui Gong
 * @Date: 2/18/20
 */
@Service("iCategoryService")
public class categoryServiceImpl implements ICategoryService {
    private Logger logger = LoggerFactory.getLogger(categoryServiceImpl.class);

    @Autowired
    public CategoryMapper categoryMapper;

    @Override
    public ServerResponse<String> addCategory(Integer parentId, String categoryName) {
        if (parentId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("参数错误，请重试");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setParentId(parentId);
        category.setStatus(true);

        int resultCount = categoryMapper.insert(category);

        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("添加品类成功");
        } else {
            return ServerResponse.createByErrorMessage("添加品类失败，请重试");
        }
    }

    @Override
    public ServerResponse<String> updateCategoryName(Integer categoryId, String categoryName) {
        if (categoryId == null || StringUtils.isBlank(categoryName)) {
            return ServerResponse.createByErrorMessage("参数错误，请重试");
        }

        Category category = new Category();
        category.setName(categoryName);
        category.setId(categoryId);

        int resultCount = categoryMapper.updateByPrimaryKeySelective(category);

        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("更新品类成功");
        } else {
            return ServerResponse.createByErrorMessage("更新品类失败");
        }
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId) {
        if (categoryId == null) {
            return ServerResponse.createByErrorMessage("参数错误，请重试");
        }

        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);

        if (CollectionUtils.isEmpty(categoryList)) {
            logger.info("未找到相关类别");
        }

        return ServerResponse.createBySuccess(categoryList);
    }

    @Override
    public ServerResponse getDeepChildrenCategory(Integer categoryId) {
        Set<Category> categorySet = Sets.newHashSet();

        findChildrenCategory(categorySet, categoryId);


        List<Integer> categoryIdList = Lists.newArrayList();
        if (categoryId != null) {

            for (Category categoryItem :
                    categorySet) {
                categoryIdList.add(categoryItem.getId());
            }
        }

        return ServerResponse.createBySuccess(categoryIdList);
    }

    private Set<Category> findChildrenCategory(Set<Category> categorySet, Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if (category != null) {
            categorySet.add(category);
        }

        List<Category> categoryList = categoryMapper.selectCategoryChildrenByParentId(categoryId);
        for (Category categoryItem : categoryList
        ) {
            findChildrenCategory(categorySet, categoryItem.getId());
        }

        return categorySet;

    }


}
