package com.sky.mapper;

import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

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
}
