package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public void addCart(ShoppingCartDTO shoppingCartDTO) {
        //创建ShoppingCart对象，拷贝属性值
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);

        //判断该商品是否已经存在购物车，条件：dishId + dishFlavor + userId
            //只查当前用户自己的购物车
        shoppingCart.setUserId(BaseContext.getCurrentId());
        ShoppingCart cart = shoppingCartMapper.selectBy(shoppingCart);
        if (cart == null){//没有该商品数据
            //判断添加的是套餐还是菜品
            if (shoppingCartDTO.getDishId() != null){//代表新增的是菜品
                //根据菜品的id查询菜品相关信息
                Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setImage(dish.getImage());
            }else{//代表添加的是套餐
                //根据菜品的id查询菜品相关信息
                Setmeal setmeal = setmealMapper.querySetmealById(shoppingCartDTO.getSetmealId());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setImage(setmeal.getImage());

            }
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1); //数量--》到底是 1还是 +1 ？，判断商品是否已存在购物车

            //将数据存入shopping_cart表中
            shoppingCartMapper.insert(shoppingCart);
        }else{//有该商品数据
            //将原来的购物车商品数量 +1，调用Mapper更新方法
            cart.setNumber(cart.getNumber() + 1);
            shoppingCartMapper.updateNumber(cart);

        }

    }

    /**
     * 根据用户id查询购物车
     * @return
     */
    @Override
    public List<ShoppingCart> listCart() {
        //根据当前用户id查询购物车
        return shoppingCartMapper.listCart(BaseContext.getCurrentId());
    }

    /**
     * 删除购物车中的商品
     * @param shoppingCartDTO
     */
    @Override
    public void subCart(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //判断是菜品还是套餐
        if (shoppingCartDTO.getDishId() != null){//判断为菜品

            //查询购物车中该菜品--》数量
            ShoppingCart cart = shoppingCartMapper.selectBy(shoppingCart);
            //判断购物车中要删除的菜品的数量
            if (cart.getNumber() == 1){
                shoppingCartMapper.deleteCart(cart);
            }else {
                cart.setNumber(cart.getNumber() - 1);
                shoppingCartMapper.updateNumber(cart);
            }

        }else{//判断为套餐
            //查询购物车中该套餐--》数量
            ShoppingCart cart = shoppingCartMapper.selectBy(shoppingCart);
            //判断购物车中要删除的套餐的数量
            if (cart.getNumber() == 1){
                shoppingCartMapper.deleteCart(cart);
            }else {
                cart.setNumber(cart.getNumber() - 1);
                shoppingCartMapper.updateNumber(cart);
            }

        }

    }

    /**
     * 清空购物车
     */
    @Override
    public void cleanCart() {
        shoppingCartMapper.cleanCart(BaseContext.getCurrentId());
    }
}
