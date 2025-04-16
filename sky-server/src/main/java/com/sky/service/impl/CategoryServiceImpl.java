package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增分类
     * @param categoryDTO
     */
    @Override
    public void addCategory(CategoryDTO categoryDTO) {
        Category category = new Category();

        //属性拷贝
        BeanUtils.copyProperties(categoryDTO, category);
       //设置启用状态默认为0
        category.setStatus(StatusConstant.DISABLE);
        //补全属性
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        //设置分页参数
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        //类型转换
        Page<Category> page= categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void deleteById(Long id) {
        //判断该分类下有没有菜品，没有可删，有则不可删除
        Integer count = dishMapper.countByCategoryId(id);
        log.info("该分类下有 {} 样菜品",count);
        if (count>0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }

        //判断该分类是否关联套餐
        count = setmealMapper.countByCategoryId(id);
        log.info("该分类关联了 {} 样套餐",count);
        if (count>0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        categoryMapper.deleteCategoryId(id);

    }

    /**
     * 修改分类信息
     * @param categoryDTO
     */
    @Override
    public void updateCategory(CategoryDTO categoryDTO) {
        //属性拷贝
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);

        //属性补全
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.updateCategory(category);
    }

    /**
     * 修改状态
     * @param id
     * @param status
     */
    @Override
    public void updateCategoryStatus(Long id, Integer status) {
        Category category= new Category();
        category.setId(id);
        category.setStatus(status);
        category.setUpdateTime(LocalDateTime.now());
        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.updateCategory(category);
    }

    /**
     * 根据类型查询
     * @param type
     * @return
     */
    @Override
    public List<Category> getByType(Integer type) {
        return categoryMapper.getByType(type);
    }


}
