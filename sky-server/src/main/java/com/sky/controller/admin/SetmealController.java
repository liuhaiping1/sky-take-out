package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Api(tags = "套餐管理相关接口")
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param dto
     * @return
     */
    //@CacheEvict：清理指定分类下面的套餐缓存
    @CacheEvict(cacheNames = "setmeal",key = "#dto.categoryId")
    @ApiOperation("新增套餐")
    @PostMapping
    public Result addSetmeal(@RequestBody SetmealDTO dto){
        log.info("新增套餐{}",dto);
        setmealService.addSetmeal(dto);
        return Result.success();
    }

    /**
     * 套餐分页查询
     * @param dto
     * @return
     */
    @ApiOperation("套餐分页查询")
    @GetMapping("/page")
    public Result<PageResult> setmealPageQuery(SetmealPageQueryDTO dto){
        log.info("套餐分页查询{}",dto);
        PageResult pageResult =setmealService.setmealPageQuery(dto);
        return Result.success(pageResult);
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    //@CacheEvict: 清除setmeal下全部缓存数据
    @CacheEvict(cacheNames = "setmeal",allEntries = true)
    @ApiOperation("批量删除套餐")
    @DeleteMapping
    public Result deleteSetmeal(@RequestParam List<Long> ids){
        log.info("批量批量删除套餐{}",ids);
        setmealService.deleteSetmeal(ids);
        return Result.success();
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @ApiOperation("根据id查询套餐")
    @GetMapping("/{id}")
    public Result<SetmealVO> querySetmealById(@PathVariable Long id){
        log.info("查询套餐{}",id);
        SetmealVO setmealVO = setmealService.querySetmealById(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐信息
     * @param setmealDTO
     * @return
     */
    //@CacheEvict: 清除setmeal下全部缓存数据
    @CacheEvict(cacheNames = "setmeal",allEntries = true)
    @ApiOperation("修改套餐信息")
    @PutMapping
    public Result updateSetmeal(@RequestBody SetmealDTO setmealDTO){
        log.info("修改套餐信息{}",setmealDTO);
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 修改套餐状态
     * @param status
     * @param id
     * @return
     */
    //@CacheEvict: 清除setmeal下全部缓存数据
    @CacheEvict(cacheNames = "setmeal",allEntries = true)
    @ApiOperation("修改套餐状态")
    @PostMapping("status/{status}")
    public Result updateSetmealStatus(@PathVariable Integer status,Long id){
        log.info("修改id为{}的套餐状态为{}",id,status);
        setmealService.updateSetmealStatus(status,id);
        return Result.success();
    }

}
