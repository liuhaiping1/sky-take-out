package com.sky.service.impl;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.anno.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {


    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;


    /**
     * 新增菜品
     * @param dishDTO
     */
    @Transactional//开启事务，涉及多张表的增删改操作
    public void addDish(DishDTO dishDTO) {
        //1.构造菜品基本信息数据，将其存入dish表中
        Dish dish = new Dish();
        //1.1拷贝属性值
        BeanUtils.copyProperties(dishDTO,dish);
        //1.2 调用Mapper保存方法
        dishMapper.insert(dish);

        log.info("dishId={}",dish.getId());

        //2.构造菜品口味列表数据，将其存入dish_flavor表中
        List<DishFlavor> dishFlavorsList = dishDTO.getFlavors();
        if (dishFlavorsList != null && !dishFlavorsList.isEmpty()){
            //2.1 补充关联的菜品id
            dishFlavorsList.forEach(flavor ->{
                flavor.setDishId(dish.getId());
            });
            //2.2 调用mapper,批量插入
            dishFlavorMapper.insertBatch(dishFlavorsList);
        }

    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult dishPageQuery(DishPageQueryDTO dishPageQueryDTO) {
        //设置分页参数
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.dishPageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除菜品
     * @param ids
     */
    @Transactional
    @Override
    public void deleteDish(List<Long> ids) {
        //删除菜品
        dishMapper.deleteDish(ids);

        //删除口味
        dishFlavorMapper.deleteFlavor(ids);
    }

    /**
     * 根据id查询菜品及口味
     * @param id
     * @return
     */
    @Override
    public DishVO getById(Long id) {
        //根据id查询菜品
        Dish dish =dishMapper.getById(id);
        DishVO dishVO = new DishVO();
        //属性拷贝
        BeanUtils.copyProperties(dish,dishVO);
        //根据id查询口味，并添加到DishVO对象中
        dishVO.setFlavors(dishFlavorMapper.getByDishId(id));
        return dishVO;
    }


    /**
     * 修改菜品售卖状态
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        Dish dish = Dish.builder()
                .id(id)
                .status(status)
                .build();
        dishMapper.update(dish);
    }

    /**
     * 修改菜品
     * @param dishDTO
     */
    @Transactional
    @Override
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);

        //删除口味
        dishFlavorMapper.deleteFlavor(Arrays.asList(dishDTO.getId()));

        //构造菜品口味列表数据，将其存入dish_flavor表中
        List<DishFlavor> dishFlavorsList = dishDTO.getFlavors();
        //2.1 补充关联的菜品id
        if (dishFlavorsList !=null && !dishFlavorsList.isEmpty()){
            dishFlavorsList.forEach(flavor ->{
                flavor.setDishId(dishDTO.getId());
            });
            //2.2 调用mapper,批量插入
            dishFlavorMapper.insertBatch(dishFlavorsList);
        }

    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        return dishMapper.getByCategoryId(categoryId);
    }

}
