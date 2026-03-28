package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询套餐id
     */
    List<Long> getSetmealIdsByDishId(List<Long> ids);

    /**
     * 插入套餐菜品关系
     *
     * @param setmealDishes
     */
    void insertBatch(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id批量删除关系表
     *
     * @param setMealIds
     */
    void deleteBatch(List<Long> setMealIds);

    /**
     * 根据套餐id查询所有套餐中的商品关联关系
     *
     * @param setmealId
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getBySetmealId(Long setmealId);
}
