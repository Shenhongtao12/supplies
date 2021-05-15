package com.sht.supplies.service;

import com.sht.supplies.common.BaseCommon;
import com.sht.supplies.entity.OutStock;
import com.sht.supplies.mapper.OutStockMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;

/**
 * @author Aaron.H.Shen
 * @date 5/13/2021 4:42 PM
 */
@Slf4j
@Service
public class OutStockService {

    @Autowired
    private OutStockMapper outStockMapper;

    @Autowired
    private GoodsService goodsService;

    /**
     * 正常情况下加注解@Transactional和try catch捕获异常会让注解失效
     * 解决办法就是在catch语句块中添加
     * propagation = Propagation.REQUIRED -- 支持当前事务，如果当前没有事务，就新建一个事务
     * @param outStock 出库
     * @return bool
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean add(OutStock outStock) {
        outStock.setInDate(LocalDateTime.now());

        try {
            if (goodsService.updateInventory(outStock.getGoodsId(), -outStock.getAmount())) {
                outStockMapper.insertSelective(outStock);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("出库插入数据异常：" + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }
}
