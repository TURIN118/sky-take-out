package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.GoodsSalesDTO;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 创建人：  @author WNJ
 * 创建时间: 2026-03-29 08:30
 * 项目名称: sky-take-out
 * 文件名称: OrderMapper
 * Copyright:2016-2026
 */
@Mapper
public interface OrderMapper {
    /**
     * 添加订单数据
     *
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号和用户ID查询订单
     *
     * @param orderNumber
     * @param userId
     * @return
     */
    @Select("select * from orders where number = #{orderNumber} and user_id = #{userId}")
    Orders getByNumberAndUserId(String orderNumber, Long userId);

    /**
     * 修改订单信息
     *
     * @param orders
     */
    void update(Orders orders);

    /**
     * 分页查询并按下单时间排序
     *
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据订单id查询订单信息
     *
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id};")
    Orders getById(Long id);


    /**
     * 根据状态统计订单数量
     *
     * @param status
     * @return
     */
    @Select("select count(id) from orders where status = #{status}")
    Integer countStatus(Integer status);

    /**
     * 根据订单状态和下单时间查询订单
     *
     * @param status
     * @param orderTime
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{orderTime} ")
    List<Orders> getByStatusAndOrderTimeLT(Integer status, LocalDateTime orderTime);

    /**
     * 根据动态条件统计营业额数据
     *
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * 根据动态条件统计订单数
     *
     * @param map
     * @return
     */
    Integer countByMap(Map map);


    /**
     * 统计指定时间内的销量排行前10榜
     *
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> getSaleTop10(LocalDateTime begin, LocalDateTime end);
}
