package com.sky.mapper;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface ShoppingCartMapper {
    /**
     * 查询购物车
     * @param shoppingCart
     * @return
     */
    ShoppingCart selectBy(ShoppingCart shoppingCart);

    /**
     * 新增商品进入购物车
     * @param shoppingCart
     */
    void insert(ShoppingCart shoppingCart);

    /**
     * 根据id更新购物车数量
     * @param cart
     */
    void updateNumber(ShoppingCart cart);

    /**
     * 根据用户id查询购物车
     * @param userId
     * @return
     */
    List<ShoppingCart> listCart(Long userId);

    /**
     * 单个删除购物车中的商品
     * @param shoppingCart
     */
    void deleteCart(ShoppingCart shoppingCart);

    /**
     * 清空购物车
     * @param userId
     */
    void cleanCart(Long userId);
}
