package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.anno.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@Slf4j
@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐
     * @param dto
     */
    @Transactional
    @Override
    public void addSetmeal(SetmealDTO dto) {
        //新增套餐
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(dto,setmeal);
        setmealMapper.add(setmeal);
        log.info("setmealId:{}",setmeal.getId());
        //新增套餐与菜品联系
        // 插入 setmealDish 记录
        List<SetmealDish> setmealDishList = dto.getSetmealDishes();
        if (setmealDishList != null && !setmealDishList.isEmpty()) {
            setmealDishList.forEach(setmealDish ->{
                setmealDish.setSetmealId(setmeal.getId());
            });
            setmealDishMapper.add(setmealDishList);
        }
    }

    /**
     * 套餐分页查询
     * @param dto
     * @return
     */
    @Override
    public PageResult setmealPageQuery(SetmealPageQueryDTO dto) {
        PageHelper.startPage(dto.getPage(),dto.getPageSize());
        Page<SetmealVO> setmealVO=setmealMapper.setmealPageQuery(dto);
        return new  PageResult(setmealVO.getTotal(),setmealVO.getResult());
    }

    /**
     * 批量删除套餐及其菜品
     * @param ids
     */
    @Transactional
    @Override
    public void deleteSetmeal(List<Long> ids) {
        //根据id批量删除套餐
        setmealMapper.deleteSetmeal(ids);

        //根据套餐id删除setmeal_dish记录
        setmealDishMapper.deleteSetmealDishs(ids);
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO querySetmealById(Long id) {
        SetmealVO setmealVO =new SetmealVO();
        //查询套餐信息
        Setmeal setmeal=setmealMapper.querySetmealById(id);
        //属性拷贝
        BeanUtils.copyProperties(setmeal,setmealVO);
        //查询套餐菜品信息并赋值给setmealVO
        setmealVO.setSetmealDishes(setmealDishMapper.querySetmealById(id));
        return setmealVO;
    }

    /**
     * 修改套餐信息
     * @param setmealDTO
     */
    @Override
    public void updateSetmeal(SetmealDTO setmealDTO) {
        //1.修改套餐基本信息
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.updateSetmeal(setmeal);
        //2.修改套餐菜品信息
        //1.1删除旧的菜品信息
        setmealDishMapper.deleteSetmealDishs(Arrays.asList(setmealDTO.getId()));

        //1.2重新插入菜品信息
        List<SetmealDish> setmealDishList = setmealDTO.getSetmealDishes();
        if (setmealDishList != null && !setmealDishList.isEmpty()) {
            setmealDishList.forEach(setmealDish ->{
                setmealDish.setSetmealId(setmealDTO.getId());
            });
            setmealDishMapper.add(setmealDTO.getSetmealDishes());
        }
    }

    @Override
    public void updateSetmealStatus(Integer status, Long id) {
        Setmeal setmeal = new Setmeal();
        setmeal.setStatus(status);
        setmeal.setId(id);
        setmealMapper.updateSetmeal(setmeal);
    }
}
