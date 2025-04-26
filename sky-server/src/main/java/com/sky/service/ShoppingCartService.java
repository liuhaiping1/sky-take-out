package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

public interface ShoppingCartService {
    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    void addCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查询购物车商品
     * @return
     */
    List<ShoppingCart> listCart();

    /**
     * 删除购物车中的商品
     * @param shoppingCartDTO
     */
    void subCart(ShoppingCartDTO shoppingCartDTO);

    /**
     * 清空购物车
     */
    void cleanCart();
}
