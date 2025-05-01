package com.sky.task;


import com.sky.entity.Orders;
import com.sky.mapper.OrdersMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单超时取消,订单派送完成
 */
@Component
@Slf4j
public class OrderTask {

    @Autowired
    private OrdersMapper  ordersMapper;
    /**
     * cron表达式：秒 分 时 日 月 年
     * 每分钟执行一次
     */
    @Scheduled(cron = "0 * * * * ?")
    public void orderTimeout() {
        // 查询订单状态，判断是否超时
        //select * from orders where status = 1 and order_time < LocalDateTime.now() - 15分钟;
       List<Orders> ordersList= ordersMapper.selectByStatusAndOrderTime(Orders.PENDING_PAYMENT, LocalDateTime.now().minusMinutes(15));
        //查到了数据，证明有超时的订单，修改订单状态为超时取消
       if (ordersList != null && ordersList.size() > 0){
            ordersList.forEach(orders -> {
                orders.setCancelReason("订单超时取消");
                orders.setCancelTime(LocalDateTime.now());
                orders.setStatus(Orders.CANCELLED);
                ordersMapper.update(orders);
            });
        }

    }

    /**
     * 每天凌晨一点检查一小时前的派送中的订单，并将订单修改为已完成
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void orderDelivery() {
        log.info("定时任务开始：{}", LocalDateTime.now());
        //select * from orders where status = 3 and order_time < LocalDateTime.now() - 1小时;
        List<Orders> ordersList = ordersMapper.selectByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().minusHours(1));
        if (ordersList != null && ordersList.size() > 0){
            ordersList.forEach(orders -> {
                orders.setDeliveryTime(LocalDateTime.now());
                orders.setStatus(Orders.COMPLETED);
            });
        }
    }
}
