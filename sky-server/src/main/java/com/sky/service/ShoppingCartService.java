package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * 创建人：  @author WNJ
 * 创建时间: 2026-03-28 14:36
 * 项目名称: sky-take-out
 * 文件名称: ShoppingCartService
 * Copyright:2016-2026
 */
public interface ShoppingCartService {

    /**
     * 添加购物车
     *
     * @param shoppingCartDTO
     */
    void add(ShoppingCartDTO shoppingCartDTO);

    /**
     * 查询购物车
     *
     * @return
     */
    List<ShoppingCart> showShoppingCart();

    /**
     * 清空购物车
     *
     * @return
     */
    void cleanShoppingCart();

    /**
     * 删除购物车中一个商品
     *
     * @return
     */
    void subShoppingCart(ShoppingCartDTO shoppingCartDTO);
}
