package com.sky.mapper;

import com.sky.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 创建人：  @author WNJ
 * 创建时间: 2026-03-29 08:31
 * 项目名称: sky-take-out
 * 文件名称: OrderDetailMapper
 * Copyright:2016-2026
 */
@Mapper
public interface OrderDetailMapper {

    /**
     * 批量插入订单明细数据
     *
     * @param orderDetailList
     */
    void insertBatch(List<OrderDetail> orderDetailList);
}
