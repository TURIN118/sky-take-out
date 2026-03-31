package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

/**
 * 创建人：  @author WNJ
 * 创建时间: 2026-03-31 12:38
 * 项目名称: sky-take-out
 * 文件名称: ReportServiceImpl
 * Copyright:2016-2026
 */
@Slf4j
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * 统计时间区间内的营业额数据
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        TurnoverReportVO turnoverStatistics = new TurnoverReportVO();
        //当前集合用于存放从begin到end范围内的每一天日期
        List<LocalDate> dateList = getLocalDateFromBeingToEnd(begin, end);
        //存放每天的营业额
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate localDate : dateList) {
            //查询data日期的营业额，日期当日按已完成的订单的金额
            //计算这一天的起始时间和结束时间
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            //select sum(amount) from orders where order_time > ? and order_time < ? and status = 5(已完成)
            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            map.put("status", Orders.COMPLETED);
            Double turnover = orderMapper.sumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);
        }
        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ','))
                .turnoverList(StringUtils.join(turnoverList, ','))
                .build();
    }

    /**
     * 用户统计接口
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getLocalDateFromBeingToEnd(begin, end);

        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();
        for (LocalDate localDate : dateList) {
            //计算这一天的起始时间和结束时间
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            Map map = new HashMap();
            map.put("end", endTime);
            // select count(id) from user where create_time > ? and create_time < ?
            Integer totalUserCount = userMapper.countByMap(map);
            map.put("begin", beginTime);
            // select count(id) from user where create_time < ?
            Integer newUserCount = userMapper.countByMap(map);
            newUserList.add(newUserCount);
            totalUserList.add(totalUserCount);
        }
        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ','))
                .newUserList(StringUtils.join(newUserList, ','))
                .totalUserList(StringUtils.join(totalUserList, ','))
                .build();
    }


    /**
     * 订单统计接口
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = getLocalDateFromBeingToEnd(begin, end);
        //订单总数
        Integer totalOrderCount = 0;
        //有效订单总数
        Integer validOrderCount = 0;
        // 订单完成率
        Double orderCompletionRate = 0.0;
        // 每天订单总数
        List<Integer> orderCountList = new ArrayList<>();
        // 每天有效订单数
        List<Integer> validOrderCountList = new ArrayList<>();
        for (LocalDate localDate : dateList) {
            //计算这一天的起始时间和结束时间
            LocalDateTime beginTime = LocalDateTime.of(localDate, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
            Map map = new HashMap();
            map.put("begin", beginTime);
            map.put("end", endTime);
            Integer total = orderMapper.countByMap(map);
            map.put("status", Orders.COMPLETED);
            Integer valid = orderMapper.countByMap(map);
            orderCountList.add(total);
            totalOrderCount += total;
            validOrderCountList.add(valid);
            validOrderCount += valid;
        }
        orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount;
        return OrderReportVO
                .builder()
                .dateList(StringUtils.join(dateList, ','))
                .orderCompletionRate(orderCompletionRate)
                .orderCountList(StringUtils.join(orderCountList, ','))
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .validOrderCountList(StringUtils.join(validOrderCountList, ','))
                .build();
    }

    /**
     * 查询销量排名top10接口
     *
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO getTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> saleTops = orderMapper.getSaleTop10(beginTime, endTime);
        List<String> names = saleTops.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        List<Integer> numbers = saleTops.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        return SalesTop10ReportVO
                .builder()
                .nameList(StringUtils.join(names, ','))
                .numberList(StringUtils.join(numbers, ','))
                .build();
    }

    /**
     * 当前集合用于存放从begin到end范围内的每一天日期
     *
     * @param begin
     * @param end
     * @return
     */
    private List<LocalDate> getLocalDateFromBeingToEnd(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        return dateList;
    }
}
