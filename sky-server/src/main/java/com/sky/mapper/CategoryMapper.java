package com.sky.mapper;



import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 新增分类
     * @param category
     */
    void insert(Category category);

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * 根据id删除分类
     * @param id
     */
    void deleteCategoryId(Long id);

    /**
     * 修改分类信息
     * @param categoryDTO
     */
    void updateCategory(Category categoryDTO);

    /**
     * 根据类型查询
     * @param type
     * @return
     */
    List<Category> getByType(Integer type);
}
