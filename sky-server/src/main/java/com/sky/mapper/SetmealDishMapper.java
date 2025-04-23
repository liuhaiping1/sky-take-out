package com.sky.mapper;


import com.sky.anno.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDishMapper {
    /**
     * 新增套餐与菜品联系
     * @param setmealDishList
     */
    void add(List<SetmealDish> setmealDishList);

    /**
     * 删除套餐的菜品信息
     * @param ids
     */
    void deleteSetmealDishs(List<Long> ids);

    /**
     * 根据id查询套餐菜品信息
     * @param id
     * @return
     */
    List<SetmealDish> querySetmealById(Long id);

    /**
     * 修改套餐菜品信息
     * @param setmealDishes
     */
    void updateSetmealDish(List<SetmealDish> setmealDishes);
}
