package com.sht.supplies.mapper;

import com.sht.supplies.entity.Statistical;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDate;

/**
 * @author Aaron
 * @date 2020/12/19 22:48
 */
public interface StatisticalMapper extends Mapper<Statistical> {

    /**
     * out stock
     * @param startDate 开始
     * @param endDate  结束
     * @return sum
     */
    @Select("select sum(amount) AS amount from out_stock\n" +
            "where in_date >= #{startDate} and in_date < #{endDate}")
    Integer sumOutStock(LocalDate startDate, LocalDate endDate);

    /**
     * 执行存储过程统计每月数据
     */
    void autoStatistical();
}
