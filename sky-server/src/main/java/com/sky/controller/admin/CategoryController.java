package com.sky.controller.admin;


import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@Api(tags="分类管理相关接口")
@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @ApiOperation("新增分类接口")
    @PostMapping
    public Result addCetegory(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类：{}",categoryDTO);
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 分类分页查询
     * @param categoryPageQueryDTO
     * @return
     */
    @ApiOperation("分类分页查询")
    @GetMapping("/page")
    public Result<PageResult> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO){
        log.info("分类分页查询:{}",categoryPageQueryDTO);
        PageResult pageResult=categoryService.pageQuery(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id删除分类
     * @param id
     * @return
     */
    @ApiOperation("根据id删除分类")
    @DeleteMapping
    public Result deleteById(Long id){
        log.info("根据id{}删除分类",id);
        categoryService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改分类信息
     * @param categoryDTO
     * @return
     */
    @ApiOperation("修改分类信息")
    @PutMapping
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO ){
        log.info("修改分类信息：{}",categoryDTO);
        categoryService.updateCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 启用，禁用状态
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("修改启用，禁用状态")
    @PostMapping("/status/{status}")
    public Result updateCategoryStatus(@PathVariable Integer status,Long id){
        log.info("修改{}状态为：{}",id,status);
        categoryService.updateCategoryStatus(id,status);
        return Result.success();
    }


    /**
     * 根据类型查询
     * @return
     */
    @ApiOperation("根据分类查询")
    @GetMapping("/list/{type}")
    public Result<List<Category>> getByType(@PathVariable Integer type){
        log.info("查询的类型:{}",type);
        List<Category> categoryList=categoryService.getByType(type);
        return Result.success(categoryList);
    }
}
