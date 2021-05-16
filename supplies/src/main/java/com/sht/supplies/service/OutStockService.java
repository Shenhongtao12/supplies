package com.sht.supplies.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sht.supplies.common.BaseCommon;
import com.sht.supplies.common.PageResult;
import com.sht.supplies.entity.OutStock;
import com.sht.supplies.entity.OutStockResponse;
import com.sht.supplies.entity.QueryEntity;
import com.sht.supplies.mapper.OutStockMapper;
import com.sun.org.apache.xpath.internal.operations.Bool;
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
            log.warn("insert出库插入数据异常：" + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean update(OutStock outStock) { // origin 30    out 20
        OutStock origin = outStockMapper.selectByPrimaryKey(outStock.getId());
        int inventory = outStock.getAmount() - origin.getAmount();
        outStock.setInDate(null);
        try {
            if (goodsService.updateInventory(outStock.getGoodsId(), -inventory)) {
                outStockMapper.updateByPrimaryKeySelective(outStock);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("update出库插入数据异常：" + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean delete(Integer id) {
        OutStock origin = outStockMapper.selectByPrimaryKey(id);
        try {
            if (goodsService.updateInventory(origin.getGoodsId(), origin.getAmount())) {
                outStockMapper.deleteByPrimaryKey(id);
                return true;
            }
            return false;
        } catch (Exception e) {
            log.warn("delete出库插入数据异常：" + e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return false;
        }
    }

    public Boolean exists(Integer id) {
        return outStockMapper.existsWithPrimaryKey(id);
    }

    public PageResult<OutStockResponse> findByPage(Integer goodsId, Integer userId, LocalDateTime startDateTime, LocalDateTime endDateTime, Integer page, Integer size) {
        if (size > 100) {
            size = 100;
        }
        PageHelper.startPage(page, size);
        Page<OutStockResponse> outStockPage = (Page<OutStockResponse>) outStockMapper.findByPage(new QueryEntity(goodsId, userId, startDateTime, endDateTime));
        return new PageResult<>(outStockPage.getTotal(), outStockPage.getPages(), outStockPage.getResult());
    }
}
