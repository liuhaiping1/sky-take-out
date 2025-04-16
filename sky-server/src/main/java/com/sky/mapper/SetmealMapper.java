package com.sky.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealMapper {
    /**
     * 根据id查询该分类关联的套餐数
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id=#{categoryId}")
    Integer countByCategoryId(Long id);
}
