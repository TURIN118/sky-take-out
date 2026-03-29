package com.sky.service;

import com.sky.dto.OrdersSubmitDTO;
import com.sky.vo.OrderSubmitVO;

/**
 * 创建人：  @author WNJ
 * 创建时间: 2026-03-29 07:50
 * 项目名称: sky-take-out
 * 文件名称: OrderService
 * Copyright:2016-2026
 */
public interface OrderService {


    /**
     * 用户下单
     *
     * @param ordersSubmitDTO
     * @return
     */
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);
}
