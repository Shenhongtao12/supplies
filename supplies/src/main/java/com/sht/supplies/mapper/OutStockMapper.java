package com.sht.supplies.mapper;

import com.sht.supplies.entity.OutStock;
import com.sht.supplies.entity.OutStockResponse;
import com.sht.supplies.entity.QueryEntity;
import tk.mybatis.mapper.common.Mapper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Aaron.H.Shen
 * @date 5/13/2021 4:42 PM
 */
public interface OutStockMapper extends Mapper<OutStock> {

    /**
     * find
     * @param queryParam query param
     * @return list
     */
    List<OutStockResponse> findByPage(QueryEntity queryParam);
}
