package com.sky.service.impl;

import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrdersMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    /**
     * 获取指定日期范围内的日期列表
     * @param begin
     * @param end
     * @return
     */
    private List<LocalDate> getDateList(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.isAfter(end)){//循环插入日期数据
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }


    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        //TODO 统计分析
        //1.准备日期列表数据dateList
        List<LocalDate> dateList = getDateList(begin, end);
        log.info("日期列表:{}",dateList);
        //2.准备营业额列表数据 turnoverList
        List<Double> turnoverList = new ArrayList<>();
        //营业额=订单状态已完成的订单金额
        //查询orders表，条件：订单状态为已完成，下单日期在begin和end之间
        dateList.forEach(date -> {
            //构造查询条件
            Map map = new HashMap();
            map.put("status",  Orders.COMPLETED);
            map.put("beginTime", LocalDateTime.of(date, LocalTime.MIN));
            map.put("endTime",LocalDateTime.of(date, LocalTime.MAX));
            Double turnover = ordersMapper.sumByMap(map);
            if (turnover == null){
                turnover = 0.0;
            }
            turnoverList.add(turnover);
        });

        //构造并返回turnoverReportVO对象
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        //1.准备日期列表数据dateList
        List<LocalDate> dateList = getDateList(begin, end);
        //2.准备新用户和总用户数据列表 newUserList，totalUserList
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        //查询每日新增用户数---user
        dateList.forEach(date -> {
            //构造查询条件
            Map map = new HashMap();
            map.put("beginTime", LocalDateTime.of(date, LocalTime.MIN));
            map.put("endTime", LocalDateTime.of(date, LocalTime.MAX));
            Integer newUsers = userMapper.countByMap(map);
            newUserList.add(newUsers);
            //3.totalUserList数据，总用户列表
            map.put("beginTime", null);
            map.put("endTime", LocalDateTime.of(date, LocalTime.MAX));
            Integer totalUser = userMapper.countByMap(map);
            totalUserList.add(totalUser);
        });
        //3.封装并返回VO对象
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .build();
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        //1.准备日期列表数据dateList
        List<LocalDate> dateList = getDateList(begin, end);
        //2.准备每天订单总数列表数据 orderCountList
        List<Integer> orderCountList = new ArrayList<>();
        dateList.forEach(date -> {
            //构造查询条件
            Map map = new HashMap();
            map.put("status", null);
            map.put("beginTime", LocalDateTime.of(date, LocalTime.MIN));
            map.put("endTime", LocalDateTime.of(date, LocalTime.MAX));
            Integer orderCount = ordersMapper.countByMap(map);
            orderCountList.add(orderCount);
        });
        //3.准备每天有效订单（已完成的订单）数列表数据 validOrderCountList
        List<Integer> validOrderCountList = new ArrayList<>();
        dateList.forEach(date -> {
            //构造查询条件
            Map map = new HashMap();
            map.put("status", Orders.COMPLETED);
            map.put("beginTime", LocalDateTime.of(date, LocalTime.MIN));
            map.put("endTime", LocalDateTime.of(date, LocalTime.MAX));
            Integer validOrderCount = ordersMapper.countByMap(map);
            validOrderCountList.add(validOrderCount);
        });
        //4.获取订单总数
        Integer totalOrderCount = ordersMapper.countByMap(null);
        //5.获取有效订单总数
        Map map = new HashMap();
        map.put("status", Orders.COMPLETED);
        Integer validOrderCount = ordersMapper.countByMap(map);
        //6.准备订单完成率（订单完成率=总有效订单/总订单总数）列表数据 orderCompletionRateList
        Double orderCompletionRate =(double) validOrderCount / totalOrderCount;
        //7.构造订单统计VO对象并返回
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCompletionRate(orderCompletionRate)
                .orderCountList(StringUtils.join(orderCountList, ","))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .validOrderCountList(StringUtils.join(validOrderCountList, ","))
                .build();
    }

    /**
     * 销量Top10统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO orderSalesTop10(LocalDate begin, LocalDate end) {
        //构造名称列表
        List<String> nameList = new ArrayList<>();
        //构造销量列表
        List numberList = new ArrayList<>();
        //根据对应时间查询order+order_detail表，构造商品名称列表
        Map map = new HashMap<>();
        map.put("status",Orders.COMPLETED);
        map.put("beginTime",LocalDateTime.of(begin, LocalTime.MIN));
        map.put("endTime",LocalDateTime.of(end, LocalTime.MAX));
        List<Map> List = orderDetailMapper.selectTop10Name(map);
        for (Map map1 : List){
            String name = (String) map1.get("name");
            nameList.add(name);
            Object number =map1.get("sumNum");
            numberList.add(number);
        }


        //构造SalesTop10ReportVO对象并返回
       return SalesTop10ReportVO.builder()
               .nameList(StringUtils.join(nameList, ","))
               .numberList(StringUtils.join(numberList, ","))
               .build();
    }
}
