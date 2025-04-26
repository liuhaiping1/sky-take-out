package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "购物车相关接口")
@Slf4j
@RestController
@RequestMapping("/user/shoppingCart")
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    /**
     * 添加购物车
     * @return
     */
    @ApiOperation("添加购物车")
    @PostMapping("/add")
    public Result addCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("添加购物车：{}",shoppingCartDTO);
        shoppingCartService.addCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 查询购物车商品
     * @return
     */
    @ApiOperation("查询购物车")
    @GetMapping("/list")
    public Result<List<ShoppingCart>> listCart(){
        log.info("查询购物车商品");
        List<ShoppingCart> cartList = shoppingCartService.listCart();
        return Result.success(cartList);
    }

    /**
     * 删除购物车中的商品
     * @param shoppingCartDTO
     * @return
     */
    @ApiOperation("删除购物车中的商品")
    @PostMapping("/sub")
    public Result subCart(@RequestBody ShoppingCartDTO shoppingCartDTO){
        log.info("删除购物车中的商品，{}",shoppingCartDTO);
        shoppingCartService.subCart(shoppingCartDTO);
        return Result.success();
    }

    /**
     * 清空购物车
     * @return
     */
    @ApiOperation("清空购物车")
    @DeleteMapping("/clean")
    public  Result cleanCart(){
        log.info("清空购物车");
        shoppingCartService.cleanCart();
        return Result.success();
    }
}
