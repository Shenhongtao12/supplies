package com.sht.supplies.mapper;

import com.sht.supplies.entity.InStock;
import com.sht.supplies.entity.OutStockResponse;
import com.sht.supplies.entity.QueryEntity;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author Aaron.H.Shen
 * @date 5/13/2021 4:42 PM
 */
public interface InStockMapper extends Mapper<InStock> {

    /**
     * batch save
     * @param stocks in stock
     * @return result
     */
    int batchInsert(List<InStock> stocks);

    /**
     * in stock
     * @param queryEntity
     * @return
     */
    List<OutStockResponse> findByPage(QueryEntity queryEntity);
}
