package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.MapKey;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface OrderDetailMapper {
    /**
     * 批量插入订单详情
     * @param orderDetailList
     */
    void insertBatch(List<OrderDetail> orderDetailList);

    /**
     * 根据订单id查询订单详情
     * @param orderId
     * @return
     */
    List<OrderDetail> getByOrderId(Long orderId);

    /**
     * 查询销量Top10的商品名称
     *
     * @param completeId
     * @return
     */
    List<String> selectTop10NameById(Integer completeId);


    /**
     * 查询销量Top10的商品名称
     * @param map
     * @return
     */
    @MapKey("name")
    List<Map> selectTop10Name(Map map);
}
