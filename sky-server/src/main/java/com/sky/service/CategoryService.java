package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;

import java.util.List;

public interface CategoryService {


    /**
     * 新增分类
     * @param categoryDTO
     */
    void addCategory(CategoryDTO categoryDTO);

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据id删除分类
     * @param id
     */
    void deleteById(Long id);

    /**
     * 修改分类信息
     * @param categoryDTO
     */
    void updateCategory(CategoryDTO categoryDTO);

    /**
     * 修改状态
     * @param id
     * @param status
     */
    void updateCategoryStatus(Long id, Integer status);

    /**
     * 根据类型查询
     * @param type
     * @return
     */
    List<Category> getByType(Integer type);
}
