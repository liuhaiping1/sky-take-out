package com.sky.mapper;

import com.sky.entity.Orders;

public interface OrdersMapper {
    /**
     * 插入订单数据
     * @param order
     */
    void insert(Orders order);
    /**
     * 根据订单号和用户id查询订单
     * @param orderNumber
     * @param userId
     */
    Orders getByNumberAndUserId(String orderNumber, Long userId);
    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);
}
