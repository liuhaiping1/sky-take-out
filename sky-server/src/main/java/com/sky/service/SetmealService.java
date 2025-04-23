package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    /**
     * 新增套餐
     * @param dto
     */
    void addSetmeal(SetmealDTO dto);


    /**
     * 套餐分页查询
     * @param dto
     * @return
     */
    PageResult setmealPageQuery(SetmealPageQueryDTO dto);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteSetmeal(List<Long> ids);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealVO querySetmealById(Long id);

    /**
     * 修改套餐信息
     * @param setmealDTO
     */
    void updateSetmeal(SetmealDTO setmealDTO);

    /**
     * 修改套餐状态
     * @param status
     * @param id
     */
    void updateSetmealStatus(Integer status, Long id);
}
