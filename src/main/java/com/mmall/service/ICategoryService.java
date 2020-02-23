package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Junrui Gong
 * @Date: 2/18/20
 */

public interface ICategoryService {

    ServerResponse<String> addCategory(Integer parentId, String categoryName);

    ServerResponse<String> updateCategoryName(Integer categoryId, String categoryName);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer categoryId);

    ServerResponse<List<Integer>> getDeepChildrenCategory(Integer categoryId);
}
