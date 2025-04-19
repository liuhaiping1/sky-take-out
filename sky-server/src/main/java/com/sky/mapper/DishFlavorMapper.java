package com.sky.mapper;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    /**
     * 新增菜品的口味
     * @param dishFlavorsList
     */
    void insertBatch(List<DishFlavor> dishFlavorsList);

    /**
     * 批量删除菜品口味
     * @param ids
     */
    void deleteFlavor(List<Long> ids);

    /**
     * 根据id查询菜品口味
     * @param id
     * @return
     */
    List<DishFlavor> getByDishId(Long id);

//    /**
//     * 根据id删除菜品口味
//     * @param list
//     */
//    void deleteFlavorById(List<Long> list);
}
