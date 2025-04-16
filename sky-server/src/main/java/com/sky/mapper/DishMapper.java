package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper {
    /**
     * 根据id查询该分类关联的菜品数
     * @param id
     * @return
     */
    @Select("select count(id) from dish where category_id=#{categoryId}")
    Integer countByCategoryId(Long id);
}
