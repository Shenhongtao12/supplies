package com.sht.supplies.mapper;

import com.sht.supplies.entity.Goods;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Aaron
 * @date 2020/12/19 22:48
 */
public interface GoodsMapper extends Mapper<Goods> {

    /**
     * 更新库存
     * @param goodsId
     * @param amount
     */
    @Update("UPDATE goods SET inventory = (inventory - #{amount}), sales_volume = (sales_volume + #{amount}) WHERE id = #{goodsId}")
    void updateInventory(Integer goodsId, Integer amount);

    @Select("select count(1) from goods LIMIT 1")
    Integer countGoods();
}
