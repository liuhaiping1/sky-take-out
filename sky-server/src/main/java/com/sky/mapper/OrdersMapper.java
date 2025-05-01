package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    /**
     * 分页查询历史订单
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQueryHistoryOrders(OrdersPageQueryDTO ordersPageQueryDTO);


    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    Orders getById(Long id);

    /**
     * 条件查询订单
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据状态统计订单数量
     * @param toBeConfirmed
     * @return
     */
    Integer countStatus(Integer toBeConfirmed);

    /**
     * 根据状态和下单时间查询订单
     * @param status
     * @param orderTime
     */
    List<Orders> selectByStatusAndOrderTime(Integer status, LocalDateTime orderTime);

    /**
     * 根据动态条件统计订单营业额
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * 根据动态条件统计每天的新增用户数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);


}
