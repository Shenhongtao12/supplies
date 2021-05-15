package com.sht.supplies.mapper;

import com.sht.supplies.entity.Goods;
import com.sht.supplies.entity.GoodsSelect;
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
     * @param id goodsId
     * @param amount num
     */
    @Update("UPDATE goods SET inventory = (inventory + #{amount}) WHERE id = #{id}")
    void updateInventory(Integer id, Integer amount);

    @Select("select id from goods where part_number = #{partNumber} LIMIT 1")
    Integer existsPartNumber(String partNumber);

    @Select("select id from goods where title = #{title} LIMIT 1")
    Integer existsTitle(String title);

    @Select("select id, part_number AS partNumber, title from goods")
    List<GoodsSelect> getPartNumberTitle();

    @Select("select inventory from goods WHERE id = #{id}")
    Integer selectInventory(Integer id);
}
